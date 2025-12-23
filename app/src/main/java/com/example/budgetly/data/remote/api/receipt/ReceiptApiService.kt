package com.example.budgetly.data.remote.api.receipt

import com.example.budgetly.data.remote.remote_models.receipt.ReceiptResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReceiptApiService {
    @Multipart
    @POST("process")
    suspend fun uploadReceipt(
        @Part file: MultipartBody.Part,
        @Part("e2e") e2e: RequestBody = "true".toRequestBody("text/plain". toMediaType())
    ): List<ReceiptResponse>

    @GET("process")
    suspend fun getReceipts(): List<ReceiptResponse>
}
