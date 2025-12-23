package com.example.budgetly.data.remote.remote_models.banking.token

data class TokenResponse(
    val access: String,
    val access_expires: Int = 86400, // Default: 86400 seconds (1 day)
    val refresh: String,
    val refresh_expires: Int = 2592000 // Default: 2592000 seconds (30 days)
)
