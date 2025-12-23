package com.example.budgetly.data.repositories_impl.api_repo_impl.assistant

import com.example.budgetly.data.local.database.entities.api.assistant.ChatEntity
import com.example.budgetly.data.local.datasources.db.api.assistant.AssistantResponseDataSource
import com.example.budgetly.data.local.datasources.db.api.assistant.ChatDataSource
import com.example.budgetly.data.mappers.api.assistant.toAssistantResponseEntity
import com.example.budgetly.data.mappers.api.assistant.toAssistantResponseModel
import com.example.budgetly.data.mappers.api.assistant.toAssistantResponseModelFlow
import com.example.budgetly.data.remote.datasources.retrofit.assistant.AssistantDataSource
import com.example.budgetly.domain.models.api.assistant.AssistantResponseModel
import com.example.budgetly.domain.models.api.assistant.ChatModel
import com.example.budgetly.domain.repositories.api.assistant.AssistantRepository
import com.example.budgetly.utils.log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssistantRepositoryImpl @Inject constructor(
    private val api: AssistantDataSource,
    private val gson: Gson,
    private val chatDataSource: ChatDataSource,
    private val responseDataSource: AssistantResponseDataSource
) : AssistantRepository {

    override suspend fun sendRequestAndStoreResponse(
        question: String,
        chatId: String?
    ): Result<AssistantResponseModel> {
        return try {
            val result = api.getAssistantResponse(question)
            log("result:getAssistantResponse: $result")
            result.fold(
                onSuccess = { remoteResponse ->
                    log("repository :remoteResponse: remoteResponse")
                    // Create chat if not provided
                    val targetChatId = chatId ?: run {
                        val title = question.trim().take(50)
                        val newChat = ChatEntity(title = title)
                        chatDataSource.insertChat(newChat)
                        newChat.chatId
                    }
                    val entity = remoteResponse.toAssistantResponseEntity(targetChatId,gson)
                    log("repository :entity: $entity")
                    responseDataSource.insertResponse(entity)
                    log("repository :model: ${entity.toAssistantResponseModel(gson)}")
                    Result.success(entity.toAssistantResponseModel(gson))
                },
                onFailure = {
                    log("repository :onFailure: $it")
                    Result.failure(it)
                }
            )
        } catch (e: Exception) {
            log("repository :Exception: ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    override suspend fun createNewChat(title: String?): String {
        val chat = ChatEntity(title = title?.take(50) ?: "Untitled Chat")
        chatDataSource.insertChat(chat)
        return chat.chatId
    }

    override suspend fun updateChatTitle(chatId: String, newTitle: String) {
        val chat = chatDataSource.getChatById(chatId)
        if (chat != null) {
            chatDataSource.insertChat(chat.copy(title = newTitle.take(50)))
        }
    }

    override suspend fun deleteChat(chatId: String) {
        responseDataSource.deleteResponsesByChatId(chatId)
        chatDataSource.deleteChatById(chatId)
    }
    override suspend fun deleteAllChats() {
        chatDataSource.deleteAllChats()
    }

    override suspend fun deleteResponseById(responseId: String) {
        responseDataSource.deleteResponseById(responseId)
    }

    override fun observeAllChats(): Flow<List<ChatModel>> {
        return chatDataSource.getAllChatsWithCount().map { list ->
            list.map {
                ChatModel(
                    chatId = it.chatId,
                    date = it.date,
                    title = it.title,
                    chatCount = it.chatCount
                )
            }
        }
    }

    override fun observeChatResponses(chatId: String): Flow<List<AssistantResponseModel>> {
        return responseDataSource.getResponsesByChatId(chatId).toAssistantResponseModelFlow(gson)
    }

    override suspend fun searchChatResponses(
        chatId: String,
        query: String
    ): Flow<List<AssistantResponseModel>> {
        return responseDataSource.searchResponses(chatId, query).toAssistantResponseModelFlow(gson)
    }
}
