package com.example.budgetly.data.mappers.api.assistant

import com.example.budgetly.data.remote.remote_models.assistant.TransactionResponse
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency
import com.example.budgetly.domain.models.enums.transaction.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun TransactionResponse.toTransactionModel(): TransactionModel {
    return TransactionModel(
        transactionId = transactionId.hashCode().toLong(), // Converting UUID string to Long (not ideal, but for mapping)
        accountId = accountId,
        categoryId = categoryId,
        subcategoryId = subcategoryId,
        date = date,
        amount = amount,
        type = type ?: TransactionType.Expense.name,
        frequency = frequency ?: TransactionFrequency.OneTime.name,
        currency = currency,
        description = description,
        categoryName = "",
        subCategoryName = ""
    )
}

fun TransactionModel.toTransactionResponse(): TransactionResponse {
    return TransactionResponse(
        transactionId = transactionId.toString(), // Reverts to string, note: original UUID is lost
        accountId = accountId ?: 0L,
        categoryId = categoryId ?: 0L,
        subcategoryId = subcategoryId ?: 0L,
        date = date,
        amount = amount,
        type = type,
        frequency = frequency,
        currency = currency,
        description = description
    )
}
fun List<TransactionResponse>.toTransactionModelList(): List<TransactionModel> =
    this.map { it.toTransactionModel() }

fun List<TransactionModel>.toTransactionResponseList(): List<TransactionResponse> =
    this.map { it.toTransactionResponse() }
fun Flow<List<TransactionResponse>>.toTransactionModelFlow(): Flow<List<TransactionModel>> =
    this.map { it.toTransactionModelList() }

fun Flow<List<TransactionModel>>.toTransactionResponseFlow(): Flow<List<TransactionResponse>> =
    this.map { it.toTransactionResponseList() }
