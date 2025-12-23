package com.example.budgetly.data.remote.remote_models.banking.agreements

data class EndUserAgreement(
    val id: String,
    val created: String,
    val institution_id: String,
    val max_historical_days: Int,
    val access_valid_for_days: Int,
    val access_scope: List<String>,
    val accepted: Boolean
)
