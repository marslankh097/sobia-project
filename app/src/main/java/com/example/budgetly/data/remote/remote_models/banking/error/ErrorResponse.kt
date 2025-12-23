package com.example.budgetly.data.remote.remote_models.banking.error

data class ErrorResponse(
    val summary: String,
    val detail: String,
    val type: String? = null,
    val status_code: Int
)
