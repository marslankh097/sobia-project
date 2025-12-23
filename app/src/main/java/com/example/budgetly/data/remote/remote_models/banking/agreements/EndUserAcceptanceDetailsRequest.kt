package com.example.budgetly.data.remote.remote_models.banking.agreements

data class EndUserAcceptanceDetailsRequest(
    val user_agent: String,
    val ip_address: String
)
