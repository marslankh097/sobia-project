package com.example.budgetly.ads
import android.app.Activity
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils
import com.monetization.adsmain.commons.getAdController
import com.monetization.adsmain.commons.loadAd
import com.monetization.adsmain.showRates.full_screen_ads.FullScreenAdsShowManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.controllers.AdsControllerBaseHelper
import com.monetization.core.counters.CounterManager.isCounterReached
import com.monetization.core.counters.CounterManager.isCounterRegistered
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.msgs.MessagesType
import com.monetization.interstitials.AdmobInterstitialAdsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInterManager @Inject constructor(
    private val commonAdsUtil: CommonAdsUtil // We Made this class In Helper Classes Section
) {
    fun registerMainCounter(counterKey:String){
        commonAdsUtil.registerCounter(counterKey, AdUtils.mainCounterMin.toInt(), AdUtils.mainCounterMax.toInt())
    }
    fun registerCounter(counterKey:String){
        commonAdsUtil.registerCounter(counterKey)
    }
    fun preloadAd(placementKey: String, adKey: String, activity: Activity) {
        adKey.loadAd(
            placementKey = placementKey,
            activity = activity,
        )
    }
    fun showInterstitial(
        activity: Activity,
        placementKey: String,
        adKey: String,
        counterKey: String? = null,
        instantAd: Boolean = false,
        screenName: String,
        requestNewIfAdShown:Boolean = false,
        requestNewIfNotAvailable: Boolean = true,
        preloadJustBeforeCounterReached:Boolean = false,
        onAdDismiss: (Boolean) -> Unit
    ) {
        val availableAds = AdmobInterstitialAdsManager.getAllController().filter {
            it.isAdAvailable()
        }
        val finalAdKey = if (availableAds.isNotEmpty()) {
            availableAds[0].getAdKey()
        } else {
            adKey
        }
        val controller = finalAdKey.getAdController() as? AdsControllerBaseHelper
        controller?.setDataMap(
            hashMapOf(
                "screenName" to screenName
            )
        )
        FullScreenAdsShowManager.showFullScreenAd(
            activity = activity,
            placementKey = placementKey,
            key = adKey,
            isInstantAd = instantAd,
            requestNewIfNotAvailable = requestNewIfNotAvailable ,
            requestNewIfAdShown = requestNewIfAdShown,
            uiAdsListener = object :UiAdsListener{
                override fun onAdAboutToShow(key: String) {
                    super.onAdAboutToShow(key)
                    commonAdsUtil.hideShowDialog(activity,true)
                }
            },
            counterKey = if (counterKey?.isCounterRegistered() == true) {
                counterKey
            } else {
                null
            },
            onAdDismiss = { shown: Boolean, error: MessagesType? ->
                commonAdsUtil.hideShowDialog(activity,false)
                if(preloadJustBeforeCounterReached){
                    counterKey?.let {
                        if(it.isCounterReached()){
                            preloadAd(placementKey,adKey,activity)
                        }
                    }
                }
                onAdDismiss.invoke(shown)
            },
            adType = AdType.INTERSTITIAL
        )
    }

}