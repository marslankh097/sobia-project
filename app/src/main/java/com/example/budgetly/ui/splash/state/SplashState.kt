package com.example.budgetly.ui.splash.state

data class SplashState(
    val progress: Float = 0f,
    val currentStep: SplashStep = SplashStep.AppUpdate,
    val isLoading: Boolean = true,
    val error: String? = null,
    val shouldNavigate: Boolean = false,
    val statusMessage: String = "" // Added for user feedback
)