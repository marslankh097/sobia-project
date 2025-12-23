package com.example.budgetly.domain.models.api.banking.token

data class TokenRequestModel(
    val secret_id: String,
    val secret_key: String
)