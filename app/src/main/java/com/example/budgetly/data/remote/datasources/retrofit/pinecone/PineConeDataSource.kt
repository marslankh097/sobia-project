package com.example.budgetly.data.remote.datasources.retrofit.pinecone

import com.example.budgetly.data.remote.remote_models.pinecone.PineConeRequest
import com.example.budgetly.data.remote.remote_models.pinecone.PineConeResponse

interface PineConeDataSource {
    suspend fun sendMessage(request: PineConeRequest): PineConeResponse
}
