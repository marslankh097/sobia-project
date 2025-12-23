package com.example.budgetly.data.local.database.dao.api.receipt

import androidx.room.Embedded
import androidx.room.Relation
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptItemEntity
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptResponseEntity

data class ReceiptResponseWithItems(
    @Embedded val receipt: ReceiptResponseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "receiptResponseId"
    )
    val items: List<ReceiptItemEntity>
)
