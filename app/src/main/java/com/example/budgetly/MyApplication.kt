package com.example.budgetly

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.demo.budgetly.BuildConfig
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.google.firebase.FirebaseApp
import com.monetization.adsmain.sdk.AdmobAppOpenAdsHelper
import com.monetization.adsmain.showRates.full_screen_ads.FullScreenAdsShowManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.commons.placementToAdWidgetModel
import com.monetization.core.listeners.RemoteConfigsProvider
import com.monetization.core.listeners.SdkListener
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.msgs.MessagesType
import com.monetization.core.ui.AdsWidgetData
import com.remote.firebaseconfigs.SdkRemoteConfigController
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {
    private var canShowAppOpenAd = false
    private var currentActivity: Activity? = null

    @Inject
    lateinit var commonAdsUtil: CommonAdsUtil

    companion object {
        var appContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext =  applicationContext
        FirebaseApp.initializeApp(applicationContext)
        SdkConfigs.setListener(object : SdkListener {
            override fun canLoadAd(adType: AdType, placementKey: String, adKey: String): Boolean {
                return commonAdsUtil.canLoadAd()
            }

            override fun canShowAd(adType: AdType, placementKey: String, adKey: String): Boolean {
                return  commonAdsUtil.canShowAd()
            }
        }, testModeEnable = BuildConfig.DEBUG)
        SdkConfigs.setRemoteConfigsListener(object : RemoteConfigsProvider {
            override fun getAdWidgetData(placementKey: String, adKey: String): AdsWidgetData? {
                return SdkRemoteConfigController.getRemoteConfigString(placementKey + "_Placement")
                    .placementToAdWidgetModel()
            }

            override fun isAdEnabled(placementKey: String, adKey: String, adType: AdType): Boolean {
                return SdkRemoteConfigController.getRemoteConfigBoolean(placementKey, true)
            }
        })
    }



    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        currentActivity = p0
        canShowAppOpenAd = true
    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
        canShowAppOpenAd = true
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
        canShowAppOpenAd = true
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
        currentActivity = null
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
        currentActivity = null
        canShowAppOpenAd = false
    }
     fun initOpenAppAd() {
        registerActivityLifecycleCallbacks(this)
        AdmobAppOpenAdsHelper.initOpensAds(
            onShowAppOpenAd = {
                FullScreenAdsShowManager.showFullScreenAd(
                    placementKey = RemoteConfigKeys.APP_OPEN_AD_ENABLED,
                    activity = currentActivity!!,
                    key = AdKeys.AppOpen.name,
                    counterKey = null,
                    isInstantAd = true,
                    uiAdsListener = object :UiAdsListener{
                        override fun onAdRequested(key: String) {
                            super.onAdRequested(key)
                            currentActivity?.let {
                                commonAdsUtil.hideShowDialog(activity = it, show = true)
                            }
                        }
                    },
                    onAdDismiss = { adShown: Boolean, msgType: MessagesType? ->
                        currentActivity?.let {
                            commonAdsUtil.hideShowDialog(activity = it, show = false)
                        }
                    },
                    adType = AdType.AppOpen
                )
            },
            canShowAppOpenAd = {
                canShowAppOpenAd && currentActivity != null /*&& currentActivity !is PremiumActivity && currentActivity !is SplashActivity*/
            }
        )
    }
}
