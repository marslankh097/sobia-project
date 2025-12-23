package com.example.budgetly.ui.splash.state

sealed class SplashOneTimeEvent {
    object NavigateToMain : SplashOneTimeEvent()
    object isAdsInitialized : SplashOneTimeEvent()
}