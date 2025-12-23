package com.example.budgetly.data.mappers.api.assistant

import com.example.budgetly.data.local.database.entities.api.assistant.AssistantResponseEntity
import com.example.budgetly.domain.models.api.assistant.AssistantResponseModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AssistantResponseEntity.toAssistantResponseModel(gson: Gson): AssistantResponseModel {
//    val type = object : TypeToken<List<TransactionResponse>>() {}.type
//    val rawResults: List<TransactionResponse> = gson.fromJson(rawResults, type)
    return AssistantResponseModel(
        id = id,
        originalQuestion = originalQuestion,
        generatedSql = generatedSql,
        naturalLanguageResponse = naturalLanguageResponse,
//        rawResults = rawResults.toTransactionModelList(),
        rawResults = emptyList(),
        error = error,
        chatOwnerId = chatOwnerId,
        timestamp = timestamp
    )
}


fun AssistantResponseModel.toAssistantResponseEntity(chatId: String,gson: Gson): AssistantResponseEntity {
   return AssistantResponseEntity(
        chatOwnerId = chatId,
        originalQuestion = originalQuestion,
        generatedSql = generatedSql,
        naturalLanguageResponse = naturalLanguageResponse,
        rawResults = "",
        error = error,
        timestamp = timestamp
    )
}

fun List<AssistantResponseEntity>.toAssistantResponseModelList(gson: Gson): List<AssistantResponseModel> =
    this.map { it.toAssistantResponseModel(gson) }

fun List<AssistantResponseModel>.toAssistantResponseEntityList(chatId: String,gson: Gson): List<AssistantResponseEntity> =
    this.map { it.toAssistantResponseEntity(chatId,gson) }

fun Flow<List<AssistantResponseEntity>>.toAssistantResponseModelFlow(gson: Gson): Flow<List<AssistantResponseModel>> =
    this.map { it.toAssistantResponseModelList(gson) }

fun Flow<List<AssistantResponseModel>>.toAssistantResponseEntityFlow(chatId: String,gson: Gson): Flow<List<AssistantResponseEntity>> =
    this.map { it.toAssistantResponseEntityList(chatId,gson) }
