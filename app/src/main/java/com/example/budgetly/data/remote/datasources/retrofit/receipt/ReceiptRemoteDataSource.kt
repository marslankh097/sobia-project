package com.example.budgetly.data.remote.datasources.retrofit.receipt

import com.example.budgetly.data.remote.remote_models.receipt.ReceiptResponse
import okhttp3.MultipartBody

interface ReceiptRemoteDataSource {
    suspend fun uploadReceipt(file: MultipartBody.Part): List<ReceiptResponse>
    suspend fun getReceipts(): List<ReceiptResponse>
}

