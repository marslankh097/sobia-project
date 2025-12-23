package com.example.budgetly.data.mappers.api.receipt
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptItemEntity
import com.example.budgetly.domain.models.api.receipt.ReceiptItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun ReceiptItemEntity.toReceiptItemModel(): ReceiptItemModel {
    return ReceiptItemModel(
        itemId = id,
        receiptId = receiptResponseId,
        name = name,
        quantity = quantity,
        pricePerItem = pricePerItem,
        category = category
    )
}


fun ReceiptItemModel.toReceiptItemEntity(receiptId: Long): ReceiptItemEntity {
    return ReceiptItemEntity(
        id = itemId,
        receiptResponseId = receiptId,
        name = name,
        quantity = quantity,
        pricePerItem = pricePerItem,
        category = category
    )
}

fun List<ReceiptItemEntity>.toReceiptItemModelList(): List<ReceiptItemModel> =
    this.map { it.toReceiptItemModel() }

fun List<ReceiptItemModel>.toReceiptItemEntityList(receiptId: Long): List<ReceiptItemEntity> =
    this.map { it.toReceiptItemEntity(receiptId) }

fun Flow<List<ReceiptItemEntity>>.toReceiptItemModelFlow(): Flow<List<ReceiptItemModel>> =
    this.map { it.toReceiptItemModelList() }

fun Flow<List<ReceiptItemModel>>.toReceiptItemEntityFlow(receiptId: Long): Flow<List<ReceiptItemEntity>> =
    this.map { it.toReceiptItemEntityList(receiptId) }

