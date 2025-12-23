package com.example.budgetly.ui.splash

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appupdate.AppUpdateHelper
import com.example.appupdate.listeners.SdkUpdateListener
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.MyNativeManager
import com.example.budgetly.data.local.datasources.data_store.PreferenceKeys
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.domain.usecases.system_usecases.PreferenceUseCases
import com.example.budgetly.domain.usecases.system_usecases.RemoteConfigUseCases
import com.example.budgetly.google_consent.GoogleConsentManager
import com.example.budgetly.ui.splash.events.SplashEvent
import com.example.budgetly.ui.splash.state.SplashOneTimeEvent
import com.example.budgetly.ui.splash.state.SplashState
import com.example.budgetly.ui.splash.state.SplashStep
import com.example.budgetly.utils.internet_controller.InternetController
import com.monetization.adsmain.sdk.AdmobAdsSdk
import com.monetization.adsmain.splash.AdmobSplashAdController
import com.monetization.adsmain.splash.SplashAdType
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.managers.FullScreenAdsShowListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val googleConsentManager: GoogleConsentManager,
    private val remoteConfigUseCases: RemoteConfigUseCases,
    private val preferenceUseCases: PreferenceUseCases,
    private val splashAdController: AdmobSplashAdController,
    private val nativeManager: MyNativeManager,
    private val commonAdsUtil: CommonAdsUtil,
    private val appUpdateHelper: AppUpdateHelper,
    private val internetController: InternetController,
    private val adsSdk: AdmobAdsSdk
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _oneTimeEvents = Channel<SplashOneTimeEvent>(Channel.BUFFERED)
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()

    private var progressJob: Job? = null
    private var maxTimeJob: Job? = null
    private val navigationMutex = Mutex()
    private var splashDurationMillis: Long = 0L


    // For pausing/resuming
    private var progressElapsed = 0L
    private var progressLastTick = 0L
    private val progressInterval = 30L

    private var maxElapsed = 0L
    private var maxLastTick = 0L


    private var canRequestAds = false
    private var isPremium = false
    private var isFirstSession = false
    private var splashLaunched = false
    private var isNavigating = false
    private var isAdShowing = false
    private var isAdsInitialized = false
    private var adLoadStartTime = 0L
    private var adLoaded = false

    init {
        viewModelScope.launch {
            try {
                // Load preferences asynchronously
                isPremium = preferenceUseCases.getPreferenceValue
                    .invoke(PreferenceKeys.isAppPurchased, false)
                    .first()

                isFirstSession = preferenceUseCases.getPreferenceValue
                    .invoke(PreferenceKeys.isFirstSession, true)
                    .first()

                canRequestAds = googleConsentManager.canRequestAds
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error loading preferences", e)
                // Use defaults
                isPremium = false
                isFirstSession = true
                canRequestAds = false
            }
        }
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckAppUpdate -> checkAppUpdate(event.context, event.lifecycle)
            is SplashEvent.AppUpdateCompleted -> {
                onEvent(SplashEvent.CheckConsent(event.context, event.lifecycle))
            }

            is SplashEvent.CheckConsent -> checkConsent(event.context, event.lifecycle)
            is SplashEvent.ConsentDone -> {
                onEvent(SplashEvent.InitializeAds(event.context, event.lifecycle))
            }

            SplashEvent.FetchRemoteConfig -> fetchRemoteConfig()
            SplashEvent.RemoteConfigFetched -> Unit

            is SplashEvent.InitializeAds -> initializeAds(event.context, event.lifecycle)
            is SplashEvent.AdsInitialized -> {
                viewModelScope.launch {
                    _oneTimeEvents.send(SplashOneTimeEvent.isAdsInitialized)
                }
                onEvent(SplashEvent.ShowSplashAd(event.context, event.lifecycle))
            }

            is SplashEvent.ShowSplashAd -> {
                startSplashFlow(event.context, event.lifecycle)
            }

            is SplashEvent.SplashAdDismissed -> {
                handleAdDismissed(event.context,event.shown)
            }

            is SplashEvent.AdLoaded -> {
                handleAdLoaded(event.context)
            }

            SplashEvent.MaxTimeReached -> onMaxTimeReached()
            SplashEvent.NavigateNext -> navigateNext()
        }
    }

    fun beginSplashFlow(context: Activity, lifecycle: Lifecycle) {
        if (internetController.isInternetAvailable) {
            onEvent(SplashEvent.FetchRemoteConfig)
        }
        onEvent(SplashEvent.CheckConsent(context, lifecycle))
    }

    private fun startSplashFlow(context: Activity, lifecycle: Lifecycle) {
        if (splashLaunched) return
        splashLaunched = true

        val splashTime = if (isPremium.not() && internetController.isInternetAvailable) {
            remoteConfigUseCases.getRemoteConfigValue.invoke(
                RemoteConfigKeys.SPLASH_TIME,
                AdUtils.splashTime
            )
        } else 3

        val totalDuration = splashTime * 1000L
        splashDurationMillis = totalDuration
        // Start progress animation
        startProgressAnimation(totalDuration)

        // Start max time timer (safety timeout)
        startMaxTimeTimer(totalDuration)

        preloadNativeAd(context)
        // Load and show ad (will navigate immediately when ad is dismissed)
        showSplashAd(context, lifecycle, totalDuration)
    }
    private fun preloadNativeAd(context:Activity){
        nativeManager.preLoad(RemoteConfigKeys.MAIN_NATIVE_ENABLED,context,AdKeys.MainNative.name)
    }
    private fun startProgressAnimation(duration: Long) {
        progressJob?.cancel()

        progressElapsed = 0L
        progressLastTick = System.currentTimeMillis()

        progressJob = viewModelScope.launch {
            try {
                while (progressElapsed < duration && !_state.value.shouldNavigate) {
                    delay(progressInterval)

                    val now = System.currentTimeMillis()
                    progressElapsed += (now - progressLastTick)
                    progressLastTick = now

                    val progress = (progressElapsed.toFloat() / duration).coerceAtMost(1f)

                    _state.update { it.copy(progress = progress) }
                }
            } catch (_: CancellationException) {}
        }
    }


    private fun startMaxTimeTimer(duration: Long) {
        maxTimeJob?.cancel()

        maxElapsed = 0L
        maxLastTick = System.currentTimeMillis()

        maxTimeJob = viewModelScope.launch {
            try {
                while (maxElapsed < duration) {
                    delay(50L)

                    val now = System.currentTimeMillis()
                    maxElapsed += (now - maxLastTick)
                    maxLastTick = now
                }
                onEvent(SplashEvent.MaxTimeReached)
            } catch (_: CancellationException) {}
        }
    }


    /*   private fun startProgressAnimation(duration: Long) {
           progressJob?.cancel()
           val progressInterval = 30L
           progressJob = viewModelScope.launch {
               try {
                   val totalFrames = duration / progressInterval
                   val increment = 1f / totalFrames

                   while (_state.value.progress < 1f && !_state.value.shouldNavigate) {
                       delay(progressInterval)
                       _state.update {
                           it.copy(progress = (it.progress + increment).coerceAtMost(1f))
                       }
                   }
               } catch (e: CancellationException) {
                   // Job cancelled, this is expected
               }
           }
       }

       private fun startMaxTimeTimer(duration: Long) {
           maxTimeJob?.cancel()
           maxTimeJob = viewModelScope.launch {
               try {
                   delay(duration)
                   onEvent(SplashEvent.MaxTimeReached)
               } catch (e: CancellationException) {
                   // Job cancelled, this is expected
               }
           }
       }*/

    // App Update Flow
    private fun checkAppUpdate(context: Activity, lifecycle: Lifecycle) {
        viewModelScope.launch {
            try {
                if (internetController.isInternetAvailable) {
                    _state.update {
                        it.copy(
                            currentStep = SplashStep.AppUpdate,
                            statusMessage = "Checking for updates..."
                        )
                    }

                    appUpdateHelper.checkAndStartUpdate(
                        context as AppCompatActivity,
                        object : SdkUpdateListener {
                            override fun onUpdateFailed(reason: String) {
                                Log.e("SplashViewModel", "Update failed: $reason")
                                onEvent(SplashEvent.AppUpdateCompleted(context, lifecycle))
                            }

                            override fun onUpdateStarted() {
                                onEvent(SplashEvent.AppUpdateCompleted(context, lifecycle))
                            }

                            override fun onUpdateSuccess() {
                                onEvent(SplashEvent.AppUpdateCompleted(context, lifecycle))
                            }
                        }
                    )
                } else {
                    onEvent(SplashEvent.AppUpdateCompleted(context, lifecycle))
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error checking app update", e)
                onEvent(SplashEvent.AppUpdateCompleted(context, lifecycle))
            }
        }
    }

    // Consent Flow
    private fun checkConsent(context: Activity, lifecycle: Lifecycle) {
        viewModelScope.launch {
            try {
                if (internetController.isInternetAvailable &&
                    isPremium.not() &&
                    googleConsentManager.canRequestAds.not()) {

                    _state.update {
                        it.copy(
                            currentStep = SplashStep.Consent,
                            statusMessage = "Privacy consent..."
                        )
                    }

                    googleConsentManager.consentTimeOut = remoteConfigUseCases
                        .getRemoteConfigValue.invoke(
                            RemoteConfigKeys.CONSENT_TIME,
                            AdUtils.consentTime
                        )

                    googleConsentManager.requestConsent(context) { consentResult ->
                        canRequestAds = googleConsentManager.canRequestAds
                        onEvent(SplashEvent.ConsentDone(context, lifecycle))
                    }
                } else {
                    onEvent(SplashEvent.ConsentDone(context, lifecycle))
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error requesting consent", e)
                onEvent(SplashEvent.ConsentDone(context, lifecycle))
            }
        }
    }

    // Remote Config Flow (runs asynchronously)
    private fun fetchRemoteConfig() {
        viewModelScope.launch {
            try {
                remoteConfigUseCases.fetchAndActivate {
                    onEvent(SplashEvent.RemoteConfigFetched)
                }
            } catch (e: Exception) {
                Log.e("SplashViewModel", "Error fetching remote config", e)
                onEvent(SplashEvent.RemoteConfigFetched)
            }
        }
    }

    // Ads Initialization Flow
    private fun initializeAds(context: Activity, lifecycle: Lifecycle) {
        if (isAdsInitialized) {
            onEvent(SplashEvent.AdsInitialized(context, lifecycle))
            return
        }

        if (isPremium.not()) {
            viewModelScope.launch {
                try {
                    _state.update {
                        it.copy(
                            currentStep = SplashStep.InitializingAds,
                            statusMessage = "Initializing..."
                        )
                    }

                    adsSdk.initAdsSdk(context, onInitialized = {
                        isAdsInitialized = true
                        onEvent(SplashEvent.AdsInitialized(context, lifecycle))
                    })
                } catch (e: Exception) {
                    Log.e("SplashViewModel", "Error initializing ads", e)
                    isAdsInitialized = true
                    onEvent(SplashEvent.AdsInitialized(context, lifecycle))
                }
            }
        } else {
            onEvent(SplashEvent.AdsInitialized(context, lifecycle))
        }
    }

    // Splash Ad Flow
    private fun showSplashAd(context: Activity, lifecycle: Lifecycle, duration: Long) {
        if (isAdShowing) return
        isAdShowing = true

        if (isPremium.not() && internetController.isInternetAvailable) {
            viewModelScope.launch {
                try {
                    _state.update {
                        it.copy(
                            currentStep = SplashStep.LoadingSplashAd,
                            statusMessage = "Loading..."
                        )
                    }

                    adLoadStartTime = System.currentTimeMillis()

                    val adType = if (isFirstSession) {
                        remoteConfigUseCases.getRemoteConfigValue.invoke(
                            RemoteConfigKeys.FIRST_SPLASH_AD_TYPE,
                            AdUtils.firstSplashAdType
                        )
                    } else {
                        remoteConfigUseCases.getRemoteConfigValue.invoke(
                            RemoteConfigKeys.RETURNING_SPLASH_AD_TYPE,
                            AdUtils.returningSplashAdType
                        )
                    }

                    val adKey = if (isFirstSession) {
                        AdKeys.FirstSplashAd.name
                    } else {
                        AdKeys.ReturningSplashAd.name
                    }
                    val enableKey = if (isFirstSession) {
                        RemoteConfigKeys.FIRST_SPLASH_AD_ENABLED
                    } else {
                        RemoteConfigKeys.RETURNING_SPLASH_AD_ENABLED
                    }

                    val splashAdType = when (adType) {
                        AdType.INTERSTITIAL.name -> SplashAdType.AdmobInter(adKey)
                        AdType.AppOpen.name -> SplashAdType.AdmobAppOpen(adKey)
                        else -> SplashAdType.None
                    }

                    splashAdController.showSplashAd(
                        enableKey = enableKey,
                        adType = splashAdType,
                        activity = context,
                        timeInMillis = duration,
                        normalLoadingDialog = {
                            // Ad loaded successfully
                            adLoaded = true
                            onEvent(SplashEvent.AdLoaded(context))
                        },
                        callBack = object : FullScreenAdsShowListener {
                            override fun onAdDismiss(
                                adKey: String,
                                adShown: Boolean,
                                rewardEarned: Boolean
                            ) {
                                Log.e("SplashViewModel", "onAdDismiss adShown $adShown")
                                onEvent(SplashEvent.SplashAdDismissed(context,adShown))
                            }

                            override fun onShowBlackBg(adKey: String, show: Boolean) {
                                // Handle UI if needed
                            }
                        },
                        lifecycle = lifecycle
                    )
                } catch (e: Exception) {
                    Log.e("SplashViewModel", "Error showing splash ad", e)
                    // If ad fails, don't wait for max time, navigate immediately
                    onEvent(SplashEvent.SplashAdDismissed(context,false))
                }
            }
        } else {
            // No ad to show, just wait for progress to complete
            // Max time timer will handle navigation
        }
    }

    private fun handleAdLoaded(context: Activity) {
        // Ad loaded successfully
        // Don't navigate yet - wait for ad to be dismissed
        Log.d("SplashViewModel", "Ad loaded successfully")
        progressJob?.cancel()
        maxTimeJob?.cancel()
        _state.update { it.copy(progress = 1f)}
        commonAdsUtil.hideShowDialog(context,true)
    }

    private fun handleAdDismissed(context: Activity,adShown: Boolean) {
        // Ad dismissed (either shown or failed)
        // Navigate immediately, don't wait for max time
        Log.d("SplashViewModel", "Ad dismissed, navigating immediately")
        _state.update { it.copy(progress = 1f)}
        commonAdsUtil.hideShowDialog(context,false)
        onEvent(SplashEvent.NavigateNext)
    }

    private fun onMaxTimeReached() {
        // Force navigation after max time if ad hasn't dismissed yet
        Log.d("SplashViewModel", "Max time reached, forcing navigation")
        if (!isNavigating && !adLoaded) {
            onEvent(SplashEvent.NavigateNext)
        }
    }

    private fun navigateNext() {
        viewModelScope.launch {
            navigationMutex.withLock {
                if (isNavigating) return@withLock
                isNavigating = true

                progressJob?.cancel()
                maxTimeJob?.cancel()

                _state.update {
                    it.copy(
                        progress = 1f,
                        currentStep = SplashStep.Complete,
                        shouldNavigate = true,
                        isLoading = false,
                        statusMessage = "Complete"
                    )
                }

                try {
                    _oneTimeEvents.send(SplashOneTimeEvent.NavigateToMain)
                } catch (e: Exception) {
                    Log.e("SplashViewModel", "Error sending navigation event", e)
                }
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        maxTimeJob?.cancel()
    }
    fun pauseTimers() {
        progressJob?.cancel()
        maxTimeJob?.cancel()
    }
    fun resumeTimers() {
        // Resume progress job
        if (splashDurationMillis == 0L) return       // ⛔ don't resume before timers exist
        if (progressElapsed >= splashDurationMillis) return
        if (maxElapsed >= splashDurationMillis) return
        progressLastTick = System.currentTimeMillis()
        progressJob = viewModelScope.launch {
            while (progressElapsed < splashDurationMillis && !_state.value.shouldNavigate) {
                delay(progressInterval)

                val now = System.currentTimeMillis()
                progressElapsed += (now - progressLastTick)
                progressLastTick = now

                val progress = (progressElapsed.toFloat() / splashDurationMillis).coerceAtMost(1f)
                _state.update { it.copy(progress = progress) }
            }
        }

        // Resume max timer
        maxLastTick = System.currentTimeMillis()
        maxTimeJob = viewModelScope.launch {
            while (maxElapsed < splashDurationMillis) {
                delay(50L)

                val now = System.currentTimeMillis()
                maxElapsed += (now - maxLastTick)
                maxLastTick = now
            }
            onEvent(SplashEvent.MaxTimeReached)
        }
    }
}
/*
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val googleConsentManager: GoogleConsentManager,
    private val remoteConfigUseCases: RemoteConfigUseCases,
    private val preferenceUseCases: PreferenceUseCases,
    private val splashAdController: AdmobSplashAdController,
    private val appUpdateHelper: AppUpdateHelper,
    private val internetController: InternetController,
    private val adsSdk: AdmobAdsSdk
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _oneTimeEvents = Channel<SplashOneTimeEvent>(Channel.BUFFERED)
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()

//    private var config = SplashConfig()
    private var progressJob: Job? = null
    private var maxTimeJob: Job? = null
    private var canRequestAds = false
    private var isPremium = false
    private var isFirstSession = false
    private var splashLaunched = false
    private var isNavigating = false
    private var isAdShowing = false
    private var isAdsInitialized = false

    init {
        canRequestAds = googleConsentManager.canRequestAds
        isPremium = isAppPurchased()
        isFirstSession = isFirstSession()
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckAppUpdate -> checkAppUpdate(event.context,event.lifecycle)
            is SplashEvent.AppUpdateCompleted -> {
                    onEvent(SplashEvent.CheckConsent(event.context, event.lifecycle))
            }

            is SplashEvent.CheckConsent -> checkConsent(event.context,event.lifecycle)
            is SplashEvent.ConsentDone ->{
                onEvent(SplashEvent.InitializeAds(event.context, event.lifecycle))
            }
            SplashEvent.FetchRemoteConfig -> fetchRemoteConfig()
            SplashEvent.RemoteConfigFetched -> Unit

            is SplashEvent.InitializeAds -> initializeAds(event.context,event.lifecycle)
            is SplashEvent.AdsInitialized ->  {
                onEvent(SplashEvent.ShowSplashAd(event.context,event.lifecycle))
            }

            is SplashEvent.ShowSplashAd -> {
                 startSplashFlow(event.context, event.lifecycle)
//                showSplashAd(event.context, event.lifecycle)
            }
            is SplashEvent.SplashAdDismissed -> {
                onEvent(SplashEvent.NavigateNext)
            }
            SplashEvent.MaxTimeReached -> onMaxTimeReached()
            SplashEvent.NavigateNext -> navigateNext()
        }
    }
    fun beginSplashFlow(context: Activity,lifecycle: Lifecycle){
        if(internetController.isInternetAvailable){
            onEvent(SplashEvent.FetchRemoteConfig)
        }
        onEvent(SplashEvent.CheckAppUpdate(context,lifecycle))
    }
    private fun startSplashFlow(context: Activity,lifecycle: Lifecycle) {
        if(splashLaunched) return
        splashLaunched = true
        val splashTime  = if(isPremium.not() && internetController.isInternetAvailable) remoteConfigUseCases.getRemoteConfigValue.invoke(RemoteConfigKeys.SPLASH_TIME, AdUtils.splashTime) else 3
        val totalDuration = splashTime * 1000
        startProgressAnimation(totalDuration)
        showSplashAd(context, lifecycle,totalDuration)
    }

    private fun startProgressAnimation(duration:Long) {
        progressJob?.cancel()
        val progressInterval = 30L
        progressJob = viewModelScope.launch {
            val totalFrames = duration / progressInterval
            val increment = 1f / totalFrames

            while (_state.value.progress < 1f && !_state.value.shouldNavigate) {
                delay(progressInterval)
                _state.update { it.copy(progress = (it.progress + increment).coerceAtMost(1f)) }
            }
        }
    }

    private fun startMaxTimeTimer(duration:Long) {
        maxTimeJob?.cancel()
        maxTimeJob = viewModelScope.launch {
            delay(duration)
            onEvent(SplashEvent.MaxTimeReached)
        }
    }

    // App Update Flow
    private fun checkAppUpdate(context: Activity,lifecycle: Lifecycle) {
        viewModelScope.launch {
            if(internetController.isInternetAvailable){
                _state.update { it.copy(currentStep = SplashStep.AppUpdate) }
                appUpdateHelper.checkAndStartUpdate(context as AppCompatActivity,object :SdkUpdateListener{
                    override fun onUpdateFailed(reason: String) {
                        onEvent(SplashEvent.AppUpdateCompleted(context,lifecycle))
                    }

                    override fun onUpdateStarted() {
                        onEvent(SplashEvent.AppUpdateCompleted(context,lifecycle))
                    }

                    override fun onUpdateSuccess() {
                        onEvent(SplashEvent.AppUpdateCompleted(context,lifecycle))
                    }

                })
            }else{
                onEvent(SplashEvent.AppUpdateCompleted(context,lifecycle))
            }
        }
    }

    // Consent Flow
    private fun checkConsent(context: Activity,lifecycle: Lifecycle) {
        viewModelScope.launch {
            if(internetController.isInternetAvailable && isPremium.not() && googleConsentManager.canRequestAds.not()){
                _state.update { it.copy(currentStep = SplashStep.Consent) }
                googleConsentManager.consentTimeOut = remoteConfigUseCases.getRemoteConfigValue.invoke(RemoteConfigKeys.CONSENT_TIME, AdUtils.consentTime)
                googleConsentManager.requestConsent(context){ consentResult ->
                    canRequestAds = googleConsentManager.canRequestAds
                    onEvent(SplashEvent.ConsentDone(context, lifecycle))
                }
            }else{
                onEvent(SplashEvent.ConsentDone(context, lifecycle))
            }
        }
    }

    // Remote Config Flow (runs asynchronously)
    private fun fetchRemoteConfig() {
        viewModelScope.launch {
            remoteConfigUseCases.fetchAndActivate{
                onEvent(SplashEvent.RemoteConfigFetched)
            }
        }
    }
    // Ads Initialization Flow
    private fun initializeAds(context: Activity,lifecycle: Lifecycle) {
        if(isAdsInitialized) return
        if(isPremium.not()){
            viewModelScope.launch {
                _state.update { it.copy(currentStep = SplashStep.InitializingAds) }
                adsSdk.initAdsSdk(context, onInitialized = {
                    isAdsInitialized = true
                    onEvent(SplashEvent.AdsInitialized(context,lifecycle))
                })
            }
        }else{
            onEvent(SplashEvent.AdsInitialized(context,lifecycle))
        }
    }
    private fun isFirstSession(): Boolean {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.isFirstSession,true).first()
        }
    }
    // Splash Ad Flow
    private fun showSplashAd(context: Activity, lifecycle: Lifecycle,duration: Long) {
        if(isAdShowing) return
        isAdShowing = true
        if(isPremium.not() && internetController.isInternetAvailable){
            viewModelScope.launch {
                _state.update { it.copy(currentStep = SplashStep.LoadingSplashAd) }
                val adType = if(isFirstSession){
                    remoteConfigUseCases.getRemoteConfigValue.invoke(RemoteConfigKeys.FIRST_SPLASH_AD_TYPE, AdUtils.firstSplashAdType)
                }else{
                    remoteConfigUseCases.getRemoteConfigValue.invoke(RemoteConfigKeys.RETURNING_SPLASH_AD_TYPE, AdUtils.returningSplashAdType)
                }
                val adKey = if(isFirstSession) AdKeys.FirstSplashAd.name else AdKeys.ReturningSplashAd.name
                val splashAdType = when(adType){
                    AdType.INTERSTITIAL.name -> SplashAdType.AdmobInter(adKey)
                    AdType.AppOpen.name -> SplashAdType.AdmobAppOpen(adKey)
                    else -> SplashAdType.None
                }
                splashAdController.showSplashAd(
                    enableKey = true.toConfigString(),
                    adType = splashAdType,
                    activity = context,
                    timeInMillis = duration,
                    normalLoadingDialog = {

                    },
                    callBack = object : FullScreenAdsShowListener {
                        override fun onAdDismiss(adKey: String, adShown: Boolean, rewardEarned: Boolean) {
                            onEvent(SplashEvent.SplashAdDismissed(adShown))
                        }

                        override fun onShowBlackBg(adKey: String, show: Boolean) {
                            // Handle UI
                        }
                    },
                    lifecycle = lifecycle
                )
            }
        }else{
          startMaxTimeTimer(duration)
        }

    }
    private fun onMaxTimeReached() {
        // Force navigation after max time
        if (!isNavigating) {
            onEvent(SplashEvent.NavigateNext)
        }
    }
    private fun isAppPurchased(): Boolean {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.isAppPurchased,false).first()
        }
    }
    private fun navigateNext() {
        if (isNavigating) return
        isNavigating = true

        progressJob?.cancel()
        maxTimeJob?.cancel()

        _state.update {
            it.copy(
                progress = 1f,
                currentStep = SplashStep.Complete,
                shouldNavigate = true,
                isLoading = false
            )
        }

        viewModelScope.launch {
            _oneTimeEvents.send(SplashOneTimeEvent.NavigateToMain)
        }
    }

    override fun onCleared() {
        super.onCleared()
        progressJob?.cancel()
        maxTimeJob?.cancel()
    }
}*/
