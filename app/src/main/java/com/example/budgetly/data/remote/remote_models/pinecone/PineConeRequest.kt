package com.example.budgetly.data.remote.remote_models.pinecone

data class PineConeRequest(
    val messages: List<ChatMessage>,
    val stream: Boolean = false,
    val model: String = "gpt-4o"
)
