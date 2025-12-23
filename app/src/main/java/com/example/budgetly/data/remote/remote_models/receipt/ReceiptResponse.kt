package com.example.budgetly.data.remote.remote_models.receipt

data class ReceiptResponse(
    val vendor: String,
    val date: String,
    val total: Double,
    val items: List<ReceiptItem>
)