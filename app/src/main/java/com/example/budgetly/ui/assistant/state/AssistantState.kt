package com.example.budgetly.ui.assistant.state

import com.example.budgetly.domain.models.api.assistant.ChatModel

data class AssistantState(
    val query: String = "",
    val selectedChatId: String? = null,
    val uiState: AssistantUiState = AssistantUiState.Idle,
    val chats: List<ChatModel> = emptyList(),
    val showDeleteAllDialog:Boolean = false,
    val showDeleteChatDialog:Boolean = false,
    val showDeleteResponseDialog:Boolean = false,
    val startNewChatDialog:Boolean = false,
    val tempChatId:String? = null,
    val tempResponseId:String? = null
)


