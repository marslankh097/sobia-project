package com.example.budgetly.data.remote.remote_models.banking.agreements

data class EndUserAgreementRequest(
    val institution_id: String,
    val max_historical_days: Int? = 90,  // Default: 90
    val access_valid_for_days: Int? = 90, // Default: 90
    val access_scope: List<String>? = listOf("balances", "details", "transactions") // Default: ["balances", "details", "transactions"]
)
