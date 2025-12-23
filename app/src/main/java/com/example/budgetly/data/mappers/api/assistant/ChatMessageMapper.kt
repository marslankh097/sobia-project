package com.example.budgetly.data.mappers.api.assistant

import com.example.budgetly.domain.models.api.assistant.AssistantResponseModel
import com.example.budgetly.domain.models.api.assistant.ChatMessageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AssistantResponseModel.toChatMessageModelPair(): List<ChatMessageModel> {
    return listOf(
        ChatMessageModel(
            isUser = true,
            content = originalQuestion,
            responseId = null,
            timestamp = timestamp
        ),
        ChatMessageModel(
            isUser = false,
            content = naturalLanguageResponse,
            responseId = id,
            timestamp = timestamp
        )
    )
}

fun List<AssistantResponseModel>.toChatMessageModelList(): List<ChatMessageModel> {
    return this.flatMap { it.toChatMessageModelPair() }
}
fun Flow<List<AssistantResponseModel>>.toChatMessageModelFlow(): Flow<List<ChatMessageModel>> =
    this.map { it.toChatMessageModelList() }
