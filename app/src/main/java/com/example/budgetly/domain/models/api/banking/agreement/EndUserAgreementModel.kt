package com.example.budgetly.domain.models.api.banking.agreement

data class EndUserAgreementModel(
    val id: String,
    val created: String,
    val institution_id: String,
    val max_historical_days: Int,
    val access_valid_for_days: Int,
    val access_scope: List<String>,
    val accepted: Boolean
)
