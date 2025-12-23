package com.example.budgetly.domain.repositories.api.receipt

import com.example.budgetly.domain.models.api.receipt.ReceiptItemModel
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ReceiptRepository {

    // Remote
    suspend fun uploadReceipt(file: MultipartBody.Part): List<ReceiptResponseModel>

    // Insert
    suspend fun insertReceiptResponse(response: ReceiptResponseModel): Long
    suspend fun insertReceiptItems(receiptId: Long,items: List<ReceiptItemModel>)

    // Get (Flow)
    fun getAllReceiptResponses(): Flow<List<ReceiptResponseModel>>
    fun getItemsForReceipt(receiptId: Long): Flow<List<ReceiptItemModel>>

    // Get by ID
    suspend fun getReceiptById(receiptId: Long): ReceiptResponseModel?

    // Delete
    suspend fun deleteReceiptResponseById(receiptId: Long)
    suspend fun deleteReceiptItemById(itemId: Long)
    suspend fun deleteAllReceipts()

    // Update
    suspend fun updateReceiptItem(item: ReceiptItemModel)
    suspend fun updateReceiptResponse(response: ReceiptResponseModel)
}

