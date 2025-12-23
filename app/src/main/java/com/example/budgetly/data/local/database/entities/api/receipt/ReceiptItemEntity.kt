package com.example.budgetly.data.local.database.entities.api.receipt

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "receipt_item",
    foreignKeys = [
        ForeignKey(
            entity = ReceiptResponseEntity::class,
            parentColumns = ["id"],
            childColumns = ["receiptResponseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("receiptResponseId")]
)
data class ReceiptItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val receiptResponseId: Long, // foreign key
    val name: String,
    val quantity: Int,
    val pricePerItem: Double,
    val category: String,
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)