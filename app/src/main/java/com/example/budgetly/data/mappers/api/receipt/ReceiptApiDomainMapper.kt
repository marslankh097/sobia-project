package com.example.budgetly.data.mappers.api.receipt

import com.example.budgetly.data.remote.remote_models.receipt.ReceiptResponse
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// ───── Single Object Mapping ─────
fun ReceiptResponse.toReceiptResponseModel(): ReceiptResponseModel {
    return ReceiptResponseModel(
        vendor = this.vendor,
        date = this.date,
        total = this.total,
        items = this.items.toReceiptItemModelList()
    )
}
fun ReceiptResponseModel.toReceiptResponse(): ReceiptResponse {
    return ReceiptResponse(
        vendor = this.vendor,
        date = this.date,
        total = this.total,
        items = this.items.toReceiptItemList()
    )
}

// ───── List Mapping ─────

fun List<ReceiptResponse>.toReceiptResponseModelList(): List<ReceiptResponseModel> =
    this.map { it.toReceiptResponseModel() }

fun List<ReceiptResponseModel>.toReceiptResponseList(): List<ReceiptResponse> =
    this.map { it.toReceiptResponse() }

// ───── Flow Mapping ─────

fun Flow<List<ReceiptResponse>>.toReceiptResponseModelFlow(): Flow<List<ReceiptResponseModel>> =
    this.map { it.toReceiptResponseModelList() }

fun Flow<List<ReceiptResponseModel>>.toReceiptResponseFlow(): Flow<List<ReceiptResponse>> =
    this.map { it.toReceiptResponseList() }
