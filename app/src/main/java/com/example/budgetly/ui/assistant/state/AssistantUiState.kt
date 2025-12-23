package com.example.budgetly.ui.assistant.state

import com.example.budgetly.domain.models.api.assistant.AssistantResponseModel
import com.example.budgetly.domain.models.api.assistant.ChatMessageModel

sealed interface AssistantUiState {
    object Idle : AssistantUiState
    object Loading : AssistantUiState
    data class Success(
        val chatMessages: List<ChatMessageModel>,
        val rawResponses: List<AssistantResponseModel>
    ) : AssistantUiState

    data class Error(val message: String) : AssistantUiState
}
