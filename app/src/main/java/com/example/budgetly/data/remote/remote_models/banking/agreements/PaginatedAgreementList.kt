package com.example.budgetly.data.remote.remote_models.banking.agreements

import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement

data class PaginatedAgreementList(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<EndUserAgreement>
)
