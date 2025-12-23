package com.example.budgetly.ui.splash.state

sealed class SplashStep {
    object AppUpdate : SplashStep()
    object Consent : SplashStep()
    object InitializingAds : SplashStep()
    object LoadingSplashAd : SplashStep()
    object Complete : SplashStep()
}