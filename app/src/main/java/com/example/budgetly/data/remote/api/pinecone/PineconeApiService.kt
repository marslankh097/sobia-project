package com.example.budgetly.data.remote.api.pinecone

import com.example.budgetly.data.remote.remote_models.pinecone.PineConeRequest
import com.example.budgetly.data.remote.remote_models.pinecone.PineConeResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PineconeApiService {
    @POST("assistant/chat/ai-budget-assistant")
    suspend fun sendMessage(
        @Header("Api-Key") apiKey: String,
        @Body request: PineConeRequest
    ): PineConeResponse

}