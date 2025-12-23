package com.example.budgetly.data.remote.remote_models.receipt

import com.google.gson.annotations.SerializedName

data class ReceiptItem(
    val name: String,
    val quantity: Int,
    @SerializedName("price_per_item") val pricePerItem: Double,
    val category: String
)