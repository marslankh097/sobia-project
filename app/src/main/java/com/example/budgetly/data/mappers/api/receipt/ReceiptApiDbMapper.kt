package com.example.budgetly.data.mappers.api.receipt
import com.example.budgetly.data.local.database.dao.api.receipt.ReceiptResponseWithItems
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptResponseEntity
import com.example.budgetly.data.remote.remote_models.receipt.ReceiptResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//api to db mapper
fun ReceiptResponse.toReceiptResponseEntity(): ReceiptResponseEntity =
    ReceiptResponseEntity(
        vendor = this.vendor,
        date = this.date,
        total = this.total
    )

fun ReceiptResponseWithItems.toReceiptResponse(): ReceiptResponse {
   return  ReceiptResponse(
       vendor = receipt.vendor,
       date = receipt.date,
       total = receipt.total,
       items = items.toReceiptItemList()
    )
}

fun List<ReceiptResponse>.toReceiptResponseEntityList(): List<ReceiptResponseEntity> =
    this.map { it.toReceiptResponseEntity() }

fun List<ReceiptResponseWithItems>.toReceiptResponseList(): List<ReceiptResponse> =
    this.map { it.toReceiptResponse() }
fun Flow<List<ReceiptResponseWithItems>>.toReceiptResponseFlow(): Flow<List<ReceiptResponse>> =
    this.map { it.toReceiptResponseList() }

fun Flow<List<ReceiptResponse>>.toReceiptResponseEntityFlow(): Flow<List<ReceiptResponseEntity>> =
    this.map { it.toReceiptResponseEntityList() }



