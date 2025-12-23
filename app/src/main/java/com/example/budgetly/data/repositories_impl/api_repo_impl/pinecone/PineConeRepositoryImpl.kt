package com.example.budgetly.data.repositories_impl.api_repo_impl.pinecone

import com.example.budgetly.data.remote.remote_models.pinecone.PineConeRequest
import com.example.budgetly.data.mappers.api.pinecone.toChatMessage
import com.example.budgetly.data.mappers.api.pinecone.toChatMessageModel
import com.example.budgetly.data.remote.datasources.retrofit.pinecone.PineConeDataSource
import com.example.budgetly.domain.models.api.pinecone.PineConeMessageModel
import com.example.budgetly.domain.repositories.api.pinecone.PineConeRepository
import javax.inject.Inject


class PineConeRepositoryImpl @Inject constructor(
    private val dataSource: PineConeDataSource
) : PineConeRepository {

    override suspend fun sendMessages(messages: List<PineConeMessageModel>): Result<PineConeMessageModel> {
        val request = PineConeRequest(
            messages = messages.map { it.toChatMessage() },
            stream = false,
            model = "gpt-4o"
        )

        return try {
            val response = dataSource.sendMessage(request)
            Result.success(response.message.toChatMessageModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/*
class AssistantRepositoryImpl @Inject constructor(
    private val api: PineconeApiService,
    private val apiKey: String
) : AssistantRepository {

    override suspend fun sendMessages(messages: List<ChatMessageModel>): Result<ChatMessageModel> {
        val remoteMessages = messages.map { it.toChatMessage() } // 🔁 Mapping
        val request = AssistantRequest(
            messages = remoteMessages,
            stream = false,
            model = "gpt-4o"
        )
        return try {
            val response = api.sendMessage(apiKey, request)
            Result.success(response.message.toChatMessageModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}*/
