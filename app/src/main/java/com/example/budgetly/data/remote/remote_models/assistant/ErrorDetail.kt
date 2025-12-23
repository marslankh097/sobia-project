package com.example.budgetly.data.remote.remote_models.assistant

data class ErrorDetail(
    val loc: List<Any>,
    val msg: String,
    val type: String
)