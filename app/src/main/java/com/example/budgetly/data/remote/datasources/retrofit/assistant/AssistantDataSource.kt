package com.example.budgetly.data.remote.datasources.retrofit.assistant

import com.example.budgetly.data.remote.remote_models.assistant.AssistantResponse

// AssistantDataSource.kt
interface AssistantDataSource {
    suspend fun getAssistantResponse(question: String): Result<AssistantResponse>
}
