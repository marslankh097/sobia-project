package com.example.budgetly.domain.models.api.banking.requisition

data class PaginatedRequisitionListModel(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel>
)
