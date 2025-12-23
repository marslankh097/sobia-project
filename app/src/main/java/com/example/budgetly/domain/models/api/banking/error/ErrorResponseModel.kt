package com.example.budgetly.domain.models.api.banking.error

data class ErrorResponseModel(
    val summary: String,
    val detail: String,
    val type: String? = null,
    val status_code: Int
)
