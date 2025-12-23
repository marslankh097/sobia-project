package com.example.budgetly.data.mappers.api.receipt
import com.example.budgetly.data.local.database.dao.api.receipt.ReceiptResponseWithItems
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptResponseEntity
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ───── Single Object Mapping ─────

fun ReceiptResponseWithItems.toReceiptResponseModel(): ReceiptResponseModel {
    return ReceiptResponseModel(
        receiptId = receipt.id,
        vendor = receipt.vendor,
        date = receipt.date,
        total = receipt.total,
        items = items.toReceiptItemModelList()
    )
}


fun ReceiptResponseModel.toReceiptResponseEntity(): ReceiptResponseEntity {
    return ReceiptResponseEntity(
        id = receiptId,
        vendor = vendor,
        date = date,
        total = total,
    )
}

fun List<ReceiptResponseWithItems>.toReceiptResponseModelList(): List<ReceiptResponseModel> =
    this.map { it.toReceiptResponseModel() }

fun List<ReceiptResponseModel>.toReceiptResponseEntityList(): List<ReceiptResponseEntity> =
    this.map { it.toReceiptResponseEntity() }

fun Flow<List<ReceiptResponseWithItems>>.toReceiptResponseModelFlow(): Flow<List<ReceiptResponseModel>> =
    this.map { it.toReceiptResponseModelList() }

fun Flow<List<ReceiptResponseModel>>.toReceiptResponseWithItemsFlow(): Flow<List<ReceiptResponseEntity>> =
    this.map { it.toReceiptResponseEntityList() }
