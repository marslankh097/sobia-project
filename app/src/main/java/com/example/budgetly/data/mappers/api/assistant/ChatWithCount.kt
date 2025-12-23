package com.example.budgetly.data.mappers.api.assistant

data class ChatWithCount(
    val chatId: String,
    val date: Long,
    val title: String,
    val chatCount: Int
)
