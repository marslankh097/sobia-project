package com.example.budgetly.data.remote.remote_models.banking.requisition

import com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition

data class PaginatedRequisitionList(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Requisition>
)
