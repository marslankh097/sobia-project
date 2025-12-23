package com.example.budgetly.data.remote.api.assistant
import com.example.budgetly.data.remote.remote_models.assistant.AssistantRequest
import com.example.budgetly.data.remote.remote_models.assistant.AssistantResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// AssistantApiService.kt
interface AssistantApiService {

    @POST("query")
    suspend fun askAssistant(
        @Body request: AssistantRequest
    ): Response<AssistantResponse>
}
