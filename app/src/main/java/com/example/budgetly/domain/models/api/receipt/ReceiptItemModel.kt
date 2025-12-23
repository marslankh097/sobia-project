package com.example.budgetly.domain.models.api.receipt

data class ReceiptItemModel(
    val itemId:Long = 0,
    val receiptId:Long = 0,
    val name: String,
    val quantity: Int,
    val pricePerItem: Double,
    val category: String
)