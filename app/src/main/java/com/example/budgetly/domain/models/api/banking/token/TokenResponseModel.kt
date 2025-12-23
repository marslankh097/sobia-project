package com.example.budgetly.domain.models.api.banking.token

data class TokenResponseModel(
    val access: String,
    val access_expires: Int = 1, //days
    val refresh: String,
    val refresh_expires: Int = 30 //days
)