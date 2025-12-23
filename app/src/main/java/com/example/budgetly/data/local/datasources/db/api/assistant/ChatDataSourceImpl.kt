package com.example.budgetly.data.local.datasources.db.api.assistant

import com.example.budgetly.data.local.database.dao.api.assistant.ChatDao
import com.example.budgetly.data.local.database.entities.api.assistant.ChatEntity
import com.example.budgetly.data.mappers.api.assistant.ChatWithCount
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatDataSourceImpl @Inject constructor(
    private val chatDao: ChatDao
) : ChatDataSource {

    override suspend fun insertChat(chat: ChatEntity) {
        chatDao.insertChat(chat)
    }
    override fun getAllChatsWithCount(): Flow<List<ChatWithCount>> =
        chatDao.getChatsWithCount()
    override suspend fun getChatById(chatId: String): ChatEntity? {
        return chatDao.getChatById(chatId)
    }

    override fun getAllChats(): Flow<List<ChatEntity>> {
        return chatDao.getAllChats()
    }

    override suspend fun deleteChatById(chatId: String) {
        chatDao.deleteChatById(chatId)
    }

    override suspend fun deleteAllChats() {
        chatDao.deleteAllChats()
    }
}
