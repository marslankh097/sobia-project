package com.example.budgetly.data.local.datasources.db.api.assistant

import com.example.budgetly.data.local.database.dao.api.assistant.AssistantResponseDao
import com.example.budgetly.data.local.database.entities.api.assistant.AssistantResponseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssistantResponseDataSourceImpl @Inject constructor(
    private val responseDao: AssistantResponseDao
) : AssistantResponseDataSource {

    override suspend fun insertResponse(response: AssistantResponseEntity) {
        responseDao.insertResponse(response)
    }

    override suspend fun getResponseById(responseId: String): AssistantResponseEntity? {
        return responseDao.getResponseById(responseId)
    }

    override fun getResponsesByChatId(chatId: String): Flow<List<AssistantResponseEntity>> {
        return responseDao.getAllResponsesByChatId(chatId)
    }

    override suspend fun deleteResponseById(responseId: String) {
        responseDao.deleteResponseById(responseId)
    }

    override suspend fun deleteResponsesByChatId(chatId: String) {
        responseDao.deleteResponsesByChatId(chatId)
    }

    override fun searchResponses(chatId: String, query: String): Flow<List<AssistantResponseEntity>> {
        return responseDao.searchResponsesInChat(chatId, query)
    }
}
