package com.example.budgetly.domain.models.api.assistant

data class ChatModel(
    val chatId: String,
    val date: Long,
    val title: String,
    val chatCount:Int = 0
)
