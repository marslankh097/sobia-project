package com.example.budgetly.data.mappers.api.assistant

import com.example.budgetly.data.remote.remote_models.assistant.AssistantResponse
import com.example.budgetly.domain.models.api.assistant.AssistantResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//api to domain mapper
fun AssistantResponse.toAssistantResponseModel(chatId:String): AssistantResponseModel {
    return     AssistantResponseModel(
        originalQuestion = original_question,
        generatedSql = generated_sql,
        chatOwnerId = chatId,
        naturalLanguageResponse = natural_language_response,
//        rawResults = raw_results.toTransactionModelList(),
        rawResults = raw_results,
        error = error
    )
}


fun AssistantResponseModel.toAssistantResponse(): AssistantResponse =
    AssistantResponse(
        original_question = originalQuestion,
        generated_sql = generatedSql,
        natural_language_response = naturalLanguageResponse,
        raw_results = rawResults,
        error = error ?: ""
    )
fun List<AssistantResponse>.toAssistantResponseModelList(chatId: String): List<AssistantResponseModel> =
    this.map { it.toAssistantResponseModel(chatId) }

fun List<AssistantResponseModel>.toAssistantResponseList(): List<AssistantResponse> =
    this.map { it.toAssistantResponse() }
fun Flow<List<AssistantResponse>>.toAssistantResponseModelFlow(chatId: String): Flow<List<AssistantResponseModel>> =
    this.map { it.toAssistantResponseModelList(chatId) }

fun Flow<List<AssistantResponseModel>>.toAssistantResponseFlow(): Flow<List<AssistantResponse>> =
    this.map { it.toAssistantResponseList() }
