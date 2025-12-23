package com.example.budgetly.domain.models.api.banking.requisition

data class RequisitionRequestModel(
    val redirect: String?,
    val institution_id: String,
    val agreement: String,
    val reference: String,
    val user_language: String = "EN",
    val ssn: String? = null,
    val account_selection: Boolean = false,
    val redirect_immediate: Boolean = false
)

