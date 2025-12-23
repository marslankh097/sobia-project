package com.example.budgetly.ui.assistant.events

import androidx.compose.ui.text.AnnotatedString

sealed interface AssistantEvent {
    data class SetQuery(val query: String) : AssistantEvent
    object SendQuery : AssistantEvent
    object Retry : AssistantEvent

    // Chat-level events
    object ObserveAllChats : AssistantEvent
    data class CreateNewChat(val title: String? = null) : AssistantEvent
    data object ResetChatScreen : AssistantEvent
    data class UpdateChatTitle(val chatId: String, val newTitle: String) : AssistantEvent
    data object DeleteChat : AssistantEvent
    data object DeleteAllChats : AssistantEvent
    data class SetTempChatId(val chatId: String) : AssistantEvent
    data class SetTempResponseId(val responseId: String) : AssistantEvent
    data class SelectChat(val chatId: String) : AssistantEvent


    // Message-level events
    data object DeleteResponse : AssistantEvent
    data class ShowDeleteAllDialog(val show:Boolean) : AssistantEvent
    data class ShowDeleteChatDialog(val show:Boolean) : AssistantEvent
    data class ShowDeleteResponseDialog(val show:Boolean) : AssistantEvent
    data class ShowNewChatDialog(val show:Boolean) : AssistantEvent
    data class SearchInChat(val query: String) : AssistantEvent
    object ObserveChatMessages : AssistantEvent
    data class CopyText(val text: AnnotatedString) : AssistantEvent
}

