package com.example.budgetly.domain.models.api.banking.token

data class RefreshTokenResponseModel(
    val access: String,
    val access_expires: Int = 1 //days
)