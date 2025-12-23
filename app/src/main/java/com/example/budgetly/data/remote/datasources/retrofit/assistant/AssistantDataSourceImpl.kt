package com.example.budgetly.data.remote.datasources.retrofit.assistant
import com.example.budgetly.data.remote.api.assistant.AssistantApiService
import com.example.budgetly.data.remote.remote_models.assistant.AssistantRequest
import com.example.budgetly.data.remote.remote_models.assistant.AssistantResponse
import com.example.budgetly.utils.log
import javax.inject.Inject
import javax.inject.Singleton

// AssistantDataSourceImpl.kt
@Singleton
class AssistantDataSourceImpl @Inject constructor(
    private val api: AssistantApiService
) : AssistantDataSource {
    override suspend fun getAssistantResponse(question: String): Result<AssistantResponse> {
        return try {
            val response = api.askAssistant(AssistantRequest(question))
            log("datasource :response: ${response}")
            if (response.isSuccessful) {
                response.body()?.let {
                    log("datasource :isSuccessful: ${it}")
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                log("datasource :failure:API error: ${response.code()} ${response.message()}")
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            log("datasource :Exception: ${e.localizedMessage}")
            Result.failure(e)
        }
    }
}
