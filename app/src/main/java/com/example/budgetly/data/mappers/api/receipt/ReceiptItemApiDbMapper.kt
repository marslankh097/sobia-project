package com.example.budgetly.data.mappers.api.receipt
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptItemEntity
import com.example.budgetly.data.remote.remote_models.receipt.ReceiptItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//api to db mapper
fun ReceiptItem.toReceiptItemEntity(receiptId: Long): ReceiptItemEntity =
    ReceiptItemEntity(
        receiptResponseId = receiptId,
        name = name,
        quantity = quantity,
        pricePerItem = pricePerItem,
        category = category
    )

fun ReceiptItemEntity.toReceiptItem(): ReceiptItem {
   return  ReceiptItem(
       name = name,
       quantity = quantity,
       pricePerItem = pricePerItem,
       category = category
    )
}

fun List<ReceiptItem>.toReceiptItemEntityList(receiptId: Long): List<ReceiptItemEntity> =
    this.map { it.toReceiptItemEntity(receiptId) }

fun List<ReceiptItemEntity>.toReceiptItemList(): List<ReceiptItem> =
    this.map { it.toReceiptItem() }
fun Flow<List<ReceiptItemEntity>>.toReceiptItemFlow(): Flow<List<ReceiptItem>> =
    this.map { it.toReceiptItemList() }

fun Flow<List<ReceiptItem>>.toReceiptItemEntityFlow(receiptId: Long): Flow<List<ReceiptItemEntity>> =
    this.map { it.toReceiptItemEntityList(receiptId) }



