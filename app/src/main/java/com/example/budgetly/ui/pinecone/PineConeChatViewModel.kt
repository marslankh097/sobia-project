package com.example.budgetly.ui.pinecone


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.domain.models.api.pinecone.PineConeMessageModel
import com.example.budgetly.domain.usecases.api_usecases.pinecone.SendQueryUseCase
import com.example.budgetly.ui.pinecone.events.PineConeEvent
import com.example.budgetly.ui.pinecone.state.PineConeState
import com.example.budgetly.ui.pinecone.state.PineConeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PineConeChatViewModel @Inject constructor(
    private val sendQueryUseCase: SendQueryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_MESSAGES = "chat_messages"
    }

    private val _state = MutableStateFlow(
        PineConeState(
            uiState = savedStateHandle.get<List<PineConeMessageModel>>(KEY_MESSAGES)?.let {
                PineConeUiState.Success(it)
            } ?: PineConeUiState.Idle
        )
    )
    val state: StateFlow<PineConeState> = _state.asStateFlow()

    fun onEvent(event: PineConeEvent) {
        when (event) {
            is PineConeEvent.SetQuery -> {
                _state.update { it.copy(query = event.query) }
            }

            PineConeEvent.SendQuery -> {
                val queryText = _state.value.query.trim()
                if (queryText.isEmpty()) return

                viewModelScope.launch {
                    val oldMessages = (_state.value.uiState as? PineConeUiState.Success)?.messages ?: emptyList()
                    savedStateHandle.set(KEY_MESSAGES, oldMessages)

                    _state.update { it.copy(uiState = PineConeUiState.Loading) }

                    val history = oldMessages + PineConeMessageModel(isUser = true, content = queryText)
                    val result = sendQueryUseCase.sendMessages(history)

                    result.onSuccess { reply ->
                        val newMessages = oldMessages + listOf(
                            PineConeMessageModel(isUser = true, content = queryText),
                            reply
                        )
                        _state.value = PineConeState(
                            query = "",
                            uiState = PineConeUiState.Success(newMessages)
                        )
                        savedStateHandle.set(KEY_MESSAGES, newMessages)
                    }

                    result.onFailure { e ->
                        _state.update {
                            it.copy(
                                uiState = if (oldMessages.isNotEmpty()) {
                                    PineConeUiState.Success(oldMessages)
                                } else {
                                    PineConeUiState.Error(e.message ?: "Something went wrong")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
