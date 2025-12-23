package com.example.budgetly.domain.usecases.api_usecases.assistant

import com.example.budgetly.domain.models.api.assistant.AssistantResponseModel
import com.example.budgetly.domain.models.api.assistant.ChatModel
import com.example.budgetly.domain.repositories.api.assistant.AssistantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
data class AssistantUseCases @Inject constructor(
    val sendRequestAndStoreResponse: SendRequestAndStoreResponseUseCase,
    val observeAllChats: ObserveAllChatsUseCase,
    val observeChatResponses: ObserveChatResponsesUseCase,
    val createNewChat: CreateNewChatUseCase,
    val updateChatTitle: UpdateChatTitleUseCase,
    val deleteChat: DeleteChatUseCase,
    val deleteAllChats: DeleteAllChatsUseCase,
    val deleteResponseById: DeleteResponseByIdUseCase,
    val searchChatResponses: SearchChatResponsesUseCase
)

@Singleton
class SendRequestAndStoreResponseUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(question: String, chatId: String? = null): Result<AssistantResponseModel> {
        return repository.sendRequestAndStoreResponse(question, chatId)
    }
}

@Singleton
class ObserveAllChatsUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    operator fun invoke(): Flow<List<ChatModel>> = repository.observeAllChats()
}

@Singleton
class ObserveChatResponsesUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    operator fun invoke(chatId: String): Flow<List<AssistantResponseModel>> =
        repository.observeChatResponses(chatId)
}

@Singleton
class CreateNewChatUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(title: String? = null): String {
        return repository.createNewChat(title)
    }
}

@Singleton
class UpdateChatTitleUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(chatId: String, newTitle: String) {
        repository.updateChatTitle(chatId, newTitle)
    }
}

@Singleton
class DeleteChatUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(chatId: String) {
        repository.deleteChat(chatId)
    }
}
@Singleton
class DeleteAllChatsUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllChats()
    }
}

@Singleton
class DeleteResponseByIdUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(responseId: String) {
        repository.deleteResponseById(responseId)
    }
}

@Singleton
class SearchChatResponsesUseCase @Inject constructor(
    private val repository: AssistantRepository
) {
    suspend operator fun invoke(chatId: String, query: String): Flow<List<AssistantResponseModel>> {
        return repository.searchChatResponses(chatId, query)
    }
}

