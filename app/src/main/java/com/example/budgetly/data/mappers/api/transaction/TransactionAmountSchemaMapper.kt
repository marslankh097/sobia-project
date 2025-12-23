package com.example.budgetly.data.mappers.api.transaction
import com.example.budgetly.data.remote.remote_models.banking.account.TransactionAmountSchema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun TransactionAmountSchema.toTransactionAmountSchemaModel(): com.example.budgetly.domain.models.api.banking.transaction.TransactionAmountSchemaModel {
    return com.example.budgetly.domain.models.api.banking.transaction.TransactionAmountSchemaModel(
        amount = this.amount,
        currency = this.currency
    )
}

fun com.example.budgetly.domain.models.api.banking.transaction.TransactionAmountSchemaModel.toTransactionAmountSchema(): TransactionAmountSchema {
    return TransactionAmountSchema(
        amount = this.amount,
        currency = this.currency
    )
}
fun List<TransactionAmountSchema>.toTransactionAmountSchemaModelList(): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionAmountSchemaModel> =
    this.map { it.toTransactionAmountSchemaModel() }

fun List<com.example.budgetly.domain.models.api.banking.transaction.TransactionAmountSchemaModel>.toTransactionAmountSchemaList(): List<TransactionAmountSchema> =
    this.map { it.toTransactionAmountSchema() }

fun Flow<List<TransactionAmountSchema>>.toTransactionAmountSchemaModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.transaction.TransactionAmountSchemaModel>> =
    this.map { it.toTransactionAmountSchemaModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.transaction.TransactionAmountSchemaModel>>.toTransactionAmountSchemaFlow(): Flow<List<TransactionAmountSchema>> =
    this.map { it.toTransactionAmountSchemaList() }