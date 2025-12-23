package com.example.budgetly.ui.assistant

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.data.mappers.api.assistant.toChatMessageModelList
import com.example.budgetly.domain.usecases.api_usecases.assistant.AssistantUseCases
import com.example.budgetly.ui.assistant.events.AssistantEvent
import com.example.budgetly.ui.assistant.state.AssistantState
import com.example.budgetly.ui.assistant.state.AssistantUiState
import com.example.budgetly.utils.copy_controller.CopyController
import com.example.budgetly.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val useCases: AssistantUseCases,
    private val copyController: CopyController
) : ViewModel() {

    private val _assistantState = MutableStateFlow(AssistantState())
    val assistantState: StateFlow<AssistantState> = _assistantState.asStateFlow()

//    private val _tempChatId = MutableStateFlow<String?>(null)
//    val tempChatId: StateFlow<String?> = _tempChatId.asStateFlow()
//    private val _tempResponseId = MutableStateFlow<String?>(null)
//    val tempResponseId: StateFlow<String?> = _tempResponseId.asStateFlow()

    private var messageJob: Job? = null
    private var chatJob: Job? = null

    init {
        onEvent(AssistantEvent.ObserveAllChats)
    }

    fun onEvent(event: AssistantEvent) {
        when (event) {
            is AssistantEvent.SetQuery -> handleSetQuery(event.query)
            is AssistantEvent.SendQuery -> handleSendQuery()
            is AssistantEvent.SelectChat -> handleSelectChat(event.chatId)
            is AssistantEvent.ObserveChatMessages -> handleObserveChatMessages()
            is AssistantEvent.DeleteResponse -> {
                assistantState.value.tempResponseId?.let { responseId ->
                    handleDeleteResponse(responseId)
                }
            }
            is AssistantEvent.CreateNewChat -> handleCreateNewChat(event.title)
            is AssistantEvent.UpdateChatTitle -> handleUpdateChatTitle(event.chatId, event.newTitle)
            is AssistantEvent.DeleteChat ->{
                assistantState.value.tempChatId?.let {chatId->
                    handleDeleteChat(chatId)
                }
            }
            is AssistantEvent.SearchInChat -> handleSearchInChat(event.query)
            is AssistantEvent.ObserveAllChats -> handleObserveAllChats()
            is AssistantEvent.CopyText  -> handleCopyText(event.text)
            is AssistantEvent.Retry ->  handleSendQuery()
            is AssistantEvent.ShowDeleteAllDialog -> showDeleteAllDialog(event.show)
            is AssistantEvent.ShowDeleteChatDialog -> showDeleteChatDialog(event.show)
            is AssistantEvent.SetTempChatId -> setTempChatId(event.chatId)
            is AssistantEvent.SetTempResponseId -> setTempResponseId(event.responseId)
            AssistantEvent.DeleteAllChats -> deleteAllChats()
            is AssistantEvent.ShowDeleteResponseDialog -> showDeleteResponseDialog(event.show)
            AssistantEvent.ResetChatScreen -> resetChatScreen()
            is AssistantEvent.ShowNewChatDialog -> startNewChatDialog(event.show)
        }
    }

    private fun showDeleteAllDialog(show: Boolean) {
        _assistantState.update {
            it.copy(
                showDeleteAllDialog = show
            )
        }
    }
    private fun showDeleteResponseDialog(show: Boolean) {
        _assistantState.update {
            it.copy(
                showDeleteResponseDialog = show
            )
        }
    }
    private fun startNewChatDialog(show: Boolean) {
        _assistantState.update {
            it.copy(
                startNewChatDialog = show
            )
        }
    }
    private fun setTempChatId(chatId: String) {
        _assistantState.update {
            it.copy(
                tempChatId = chatId
            )
        }
    }
    private fun setTempResponseId(chatId: String) {
        _assistantState.update {
            it.copy(
                tempResponseId = chatId
            )
        }
    }
    private fun showDeleteChatDialog(show: Boolean) {
        _assistantState.update { it.copy(
            showDeleteChatDialog = show
        ) }
    }
    private fun handleSetQuery(query: String) {
        _assistantState.update { it.copy(query = query) }
    }

    private fun handleSendQuery() {
        val query = _assistantState.value.query.trim()
        if (query.isEmpty()) return

        _assistantState.update { it.copy(uiState = AssistantUiState.Loading) }

        viewModelScope.launch {
            val chatId = _assistantState.value.selectedChatId
            log("chatId:$chatId")
            val result = useCases.sendRequestAndStoreResponse(query, chatId)
            result.fold(
                onSuccess = { response ->
                    log("response:$response")
//                    val resolvedChatId = _assistantState.value.selectedChatId ?: response.id
                    val resolvedChatId =  response.chatOwnerId
                    _assistantState.update {
                        it.copy(query = "", selectedChatId = resolvedChatId)
                    }
                    onEvent(AssistantEvent.ObserveChatMessages)
                },
                onFailure = { throwable ->
                    _assistantState.update {
                        it.copy(uiState = AssistantUiState.Error(throwable.message ?: "Something went wrong"))
                    }
                }
            )
        }
    }

    private fun handleSelectChat(chatId: String) {
        _assistantState.update { it.copy(selectedChatId = chatId) }
        onEvent(AssistantEvent.ObserveChatMessages)
    }
    private fun deleteAllChats() {
        _assistantState.update { it.copy(uiState = AssistantUiState.Loading) }
        viewModelScope.launch {
            useCases.deleteAllChats()
            _assistantState.update { it.copy(selectedChatId = null, uiState = AssistantUiState.Idle) }
        }
    }

    private fun handleObserveChatMessages() {
        val chatId = _assistantState.value.selectedChatId ?: return
        log("handleObserveChatMessages: chatId: $chatId")
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            useCases.observeChatResponses(chatId).collect { responses ->
                log("handleObserveChatMessages: responses: $responses")
                val chatMessages = responses.toChatMessageModelList()
                if (responses.isEmpty()) {
                    handleDeleteChat(chatId)
                } else {
                    // Update state with valid messages
                    _assistantState.update {
                        it.copy(
                            uiState = AssistantUiState.Success(
                                chatMessages = chatMessages,
                                rawResponses = responses
                            )
                        )
                    }
                }
            }
        }
    }

    private fun handleDeleteResponse(responseId: String) {
        viewModelScope.launch {
            useCases.deleteResponseById(responseId)
        }
    }

    private fun handleCreateNewChat(title: String?) {
        viewModelScope.launch {
            val chatId = useCases.createNewChat(title)
            _assistantState.update { it.copy(selectedChatId = chatId) }
            onEvent(AssistantEvent.ObserveChatMessages)
        }
    }
    private fun resetChatScreen() {
        _assistantState.update { it.copy(selectedChatId = null, uiState = AssistantUiState.Idle) }
    }

    private fun handleUpdateChatTitle(chatId: String, newTitle: String) {
        viewModelScope.launch {
            useCases.updateChatTitle(chatId, newTitle)
        }
    }

    private fun handleDeleteChat(chatId: String) {
        viewModelScope.launch {
            useCases.deleteChat(chatId)
            if (_assistantState.value.selectedChatId == chatId) {
                _assistantState.update {
                    it.copy(selectedChatId = null, uiState = AssistantUiState.Idle)
                }
            }
        }
    }

    private fun handleSearchInChat(query: String) {
        val chatId = _assistantState.value.selectedChatId ?: return
        messageJob?.cancel()
        messageJob = viewModelScope.launch {
            useCases.searchChatResponses(chatId, query).collect { responses ->
                _assistantState.update {
                    it.copy(
                        uiState = AssistantUiState.Success(
                            chatMessages = responses.toChatMessageModelList(),
                            rawResponses = responses
                        )
                    )
                }
            }
        }
    }

    private fun handleObserveAllChats() {
        chatJob?.cancel()
        chatJob = viewModelScope.launch {
            useCases.observeAllChats().collect { chats ->
                _assistantState.update { it.copy(chats = chats) }
            }
        }
    }

    private fun handleCopyText(text: AnnotatedString) {
        copyController.copy(text)
    }
}


