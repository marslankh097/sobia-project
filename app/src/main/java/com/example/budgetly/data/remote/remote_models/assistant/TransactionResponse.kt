package com.example.budgetly.data.remote.remote_models.assistant

data class TransactionResponse(
    val transactionId: String,     // API gives UUID (varchar)
    val accountId: Long,
    val categoryId: Long,
    val subcategoryId: Long,
    val date: Long,
    val amount: String,
    val type: String?,
    val frequency: String?,
    val currency: String,
    val description: String?
)
