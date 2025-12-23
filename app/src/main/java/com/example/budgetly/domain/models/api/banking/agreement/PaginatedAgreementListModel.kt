package com.example.budgetly.domain.models.api.banking.agreement

data class PaginatedAgreementListModel(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel>
)
