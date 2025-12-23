package com.example.budgetly.data.local.datasources.db.api.assistant

import com.example.budgetly.data.local.database.entities.api.assistant.ChatEntity
import com.example.budgetly.data.mappers.api.assistant.ChatWithCount
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {
    suspend fun insertChat(chat: ChatEntity)
    suspend fun getChatById(chatId: String): ChatEntity?
    fun getAllChatsWithCount(): Flow<List<ChatWithCount>>
    fun getAllChats(): Flow<List<ChatEntity>>
    suspend fun deleteChatById(chatId: String)
    suspend fun deleteAllChats()
}
