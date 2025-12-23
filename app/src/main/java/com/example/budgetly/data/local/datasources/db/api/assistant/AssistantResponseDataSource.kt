package com.example.budgetly.data.local.datasources.db.api.assistant

import com.example.budgetly.data.local.database.entities.api.assistant.AssistantResponseEntity
import kotlinx.coroutines.flow.Flow

interface AssistantResponseDataSource {
    suspend fun insertResponse(response: AssistantResponseEntity)
    suspend fun getResponseById(responseId: String): AssistantResponseEntity?
    fun getResponsesByChatId(chatId: String): Flow<List<AssistantResponseEntity>>
    suspend fun deleteResponseById(responseId: String)
    suspend fun deleteResponsesByChatId(chatId: String)
    fun searchResponses(chatId: String, query: String): Flow<List<AssistantResponseEntity>>
}
