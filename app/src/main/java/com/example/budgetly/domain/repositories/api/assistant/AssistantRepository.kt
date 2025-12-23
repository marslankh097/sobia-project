package com.example.budgetly.domain.repositories.api.assistant

import com.example.budgetly.domain.models.api.assistant.AssistantResponseModel
import com.example.budgetly.domain.models.api.assistant.ChatModel
import kotlinx.coroutines.flow.Flow

interface AssistantRepository {

    suspend fun sendRequestAndStoreResponse(
        question: String,
        chatId: String? = null
    ): Result<AssistantResponseModel>

    suspend fun createNewChat(title: String? = null): String

    suspend fun updateChatTitle(chatId: String, newTitle: String)

    suspend fun deleteChat(chatId: String)
    suspend fun deleteAllChats()

    suspend fun deleteResponseById(responseId: String)

    fun observeAllChats(): Flow<List<ChatModel>>

    fun observeChatResponses(chatId: String): Flow<List<AssistantResponseModel>>

    suspend fun searchChatResponses(chatId: String, query: String): Flow<List<AssistantResponseModel>>
}
