package com.example.budgetly.domain.models.api.banking.agreement

data class EndUserAcceptanceDetailsRequestModel(
    val user_agent: String,
    val ip_address: String
)