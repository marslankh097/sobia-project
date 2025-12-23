package com.example.budgetly.data.mappers.api.assistant

import com.example.budgetly.data.local.database.entities.api.assistant.AssistantResponseEntity
import com.example.budgetly.data.remote.remote_models.assistant.AssistantResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//api to db mapper
fun AssistantResponse.toAssistantResponseEntity(chatId: String, gson: Gson): AssistantResponseEntity =
    AssistantResponseEntity(
        chatOwnerId = chatId,
        originalQuestion = original_question,
        generatedSql = generated_sql,
        naturalLanguageResponse = natural_language_response,
//        rawResults = gson.toJson(raw_results),
        rawResults = "",
        error = error
    )

fun AssistantResponseEntity.toAssistantResponse(gson: Gson): AssistantResponse {
//    val type = object : TypeToken<List<TransactionResponse>>() {}.type
//    val rawResults: List<TransactionResponse> = gson.fromJson(rawResults, type)
   return  AssistantResponse(
        original_question = originalQuestion,
        generated_sql = generatedSql,
        natural_language_response = naturalLanguageResponse,
//         raw_results = rawResults,
         raw_results = emptyList(),
        error = error ?: ""
    )
}

fun List<AssistantResponse>.toAssistantResponseEntityList(chatId: String,gson: Gson): List<AssistantResponseEntity> =
    this.map { it.toAssistantResponseEntity(chatId, gson) }

fun List<AssistantResponseEntity>.toAssistantResponseList(gson: Gson): List<AssistantResponse> =
    this.map { it.toAssistantResponse(gson) }
fun Flow<List<AssistantResponseEntity>>.toAssistantResponseFlow(gson: Gson): Flow<List<AssistantResponse>> =
    this.map { it.toAssistantResponseList(gson) }

fun Flow<List<AssistantResponse>>.toAssistantResponseEntityFlow(chatId: String,gson: Gson): Flow<List<AssistantResponseEntity>> =
    this.map { it.toAssistantResponseEntityList(chatId,gson) }



