package com.example.budgetly.data.remote.remote_models.banking.token

data class RefreshTokenResponse(
    val access: String,
    val access_expires: Int = 86400 // Default: 86400 seconds (1 day)
)
