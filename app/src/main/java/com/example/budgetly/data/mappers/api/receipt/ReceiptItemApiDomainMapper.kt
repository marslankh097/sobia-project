package com.example.budgetly.data.mappers.api.receipt

import com.example.budgetly.data.remote.remote_models.receipt.ReceiptItem
import com.example.budgetly.domain.models.api.receipt.ReceiptItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ───── Single Item Mapping ─────

fun ReceiptItem.toReceiptItemModel(): ReceiptItemModel {
    return ReceiptItemModel(
        name = this.name,
        quantity = this.quantity,
        pricePerItem = this.pricePerItem,
        category = this.category
    )
}

fun ReceiptItemModel.toReceiptItem(): ReceiptItem {
    return ReceiptItem(
        name = this.name,
        quantity = this.quantity,
        pricePerItem = this.pricePerItem,
        category = this.category
    )
}

// ───── List Mapping ─────

fun List<ReceiptItem>.toReceiptItemModelList(): List<ReceiptItemModel> =
    this.map { it.toReceiptItemModel() }

fun List<ReceiptItemModel>.toReceiptItemList(): List<ReceiptItem> =
    this.map { it.toReceiptItem() }

// ───── Flow Mapping ─────

fun Flow<List<ReceiptItem>>.toReceiptItemModelFlow(): Flow<List<ReceiptItemModel>> =
    this.map { it.toReceiptItemModelList() }

fun Flow<List<ReceiptItemModel>>.toReceiptItemFlow(): Flow<List<ReceiptItem>> =
    this.map { it.toReceiptItemList() }
