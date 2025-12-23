package com.example.budgetly.domain.models.api.receipt

data class ReceiptResponseModel(
    val receiptId:Long = 0L,
    val vendor: String,
    val date: String,
    val total: Double,
    val items: List<ReceiptItemModel>
)