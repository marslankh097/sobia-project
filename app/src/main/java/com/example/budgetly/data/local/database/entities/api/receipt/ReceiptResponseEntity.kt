package com.example.budgetly.data.local.database.entities.api.receipt

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_response")
data class ReceiptResponseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vendor: String,
    val date: String,
    val total: Double,
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)