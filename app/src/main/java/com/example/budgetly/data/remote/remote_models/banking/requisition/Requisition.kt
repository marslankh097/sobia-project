package com.example.budgetly.data.remote.remote_models.banking.requisition

data class Requisition(
    val id: String,
    val created: String? = null,
    val redirect: String?,
    val status: String,
    val institution_id: String,
    val agreement: String,
    val reference: String? = null,
    val accounts: List<String>? = null,
    val user_language: String? = null,
    val link: String,
    val ssn: String? = null,
    val account_selection: Boolean = false,
    val redirect_immediate: Boolean = false
)
