package com.example.budgetly.data.remote.datasources.retrofit.receipt

import com.example.budgetly.data.remote.api.receipt.ReceiptApiService
import com.example.budgetly.data.remote.remote_models.receipt.ReceiptResponse
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptRemoteDataSourceImpl @Inject constructor(
    private val api: ReceiptApiService
) : ReceiptRemoteDataSource {
    override suspend fun uploadReceipt(file: MultipartBody.Part): List<ReceiptResponse> {
        return api.uploadReceipt(file)
    }

    override suspend fun getReceipts(): List<ReceiptResponse> {
        return api.getReceipts()
    }
}
