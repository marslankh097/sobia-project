package com.example.budgetly.data.remote.datasources.retrofit.pinecone

import com.example.budgetly.data.remote.remote_models.pinecone.PineConeRequest
import com.example.budgetly.data.remote.remote_models.pinecone.PineConeResponse
import com.example.budgetly.data.remote.api.pinecone.PineconeApiService
import javax.inject.Inject
import javax.inject.Named

class PineConeDataSourceImpl @Inject constructor(
    private val api: PineconeApiService,
//    @PineConeApiKey private val apiKey: String
    @Named("pinecone_api_key") private val apiKey: String
) : PineConeDataSource {
    override suspend fun sendMessage(request: PineConeRequest): PineConeResponse {
        return api.sendMessage(apiKey, request)
    }
}
