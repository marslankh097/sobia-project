package com.example.budgetly.domain.models.api.assistant

import java.util.UUID

data class AssistantResponseModel(
    val id: String = UUID.randomUUID().toString(),
    val originalQuestion: String,
    val generatedSql: String,
    val chatOwnerId: String,
    val naturalLanguageResponse: String,
//    val rawResults: List<TransactionModel>,
    val rawResults: List<Any> = emptyList(),
    val error: String?,
    val timestamp: Long = System.currentTimeMillis()
)