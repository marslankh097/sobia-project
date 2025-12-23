package com.example.budgetly.data.remote.remote_models.assistant

data class AssistantResponse(
    val original_question: String,
    val generated_sql: String,
    val natural_language_response: String,
//    val raw_results: List<TransactionResponse>,
    val raw_results: List<Any>,
    val error: String
)