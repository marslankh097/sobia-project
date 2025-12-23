package com.example.budgetly.ui.splash.events

import android.app.Activity
import androidx.lifecycle.Lifecycle

sealed class SplashEvent {
    data class  CheckAppUpdate(val context: Activity, val lifecycle: Lifecycle) : SplashEvent()
    data class  AppUpdateCompleted(val context: Activity, val lifecycle: Lifecycle): SplashEvent()

    data class  CheckConsent(val context: Activity, val lifecycle: Lifecycle) : SplashEvent()
    data class  ConsentDone(val context: Activity, val lifecycle: Lifecycle) : SplashEvent()

    object FetchRemoteConfig : SplashEvent()
    object RemoteConfigFetched : SplashEvent()

    data class InitializeAds(val context: Activity, val lifecycle: Lifecycle) : SplashEvent()
    data class AdsInitialized(val context: Activity,val lifecycle: Lifecycle) : SplashEvent()
    data class AdLoaded(val context: Activity) : SplashEvent() // Added for when ad loads successfully

    data class  ShowSplashAd(val context: Activity, val lifecycle: Lifecycle) : SplashEvent()
    data class SplashAdDismissed(val context: Activity,val shown: Boolean) : SplashEvent()

    object MaxTimeReached : SplashEvent()
    object NavigateNext : SplashEvent()
}
