package com.example.budgetly.data.mappers.api.balance
import com.example.budgetly.data.remote.remote_models.banking.account.BalanceAfterTransactionSchema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun BalanceAfterTransactionSchema.toBalanceAfterTransactionSchemaModel(): com.example.budgetly.domain.models.api.banking.balance.BalanceAfterTransactionSchemaModel {
    return com.example.budgetly.domain.models.api.banking.balance.BalanceAfterTransactionSchemaModel(
        amount = this.amount,
        currency = this.currency
    )
}

fun com.example.budgetly.domain.models.api.banking.balance.BalanceAfterTransactionSchemaModel.toBalanceAfterTransactionSchema(): BalanceAfterTransactionSchema {
    return BalanceAfterTransactionSchema(
        amount = this.amount,
        currency = this.currency
    )
}

fun List<BalanceAfterTransactionSchema>.toBalanceAfterTransactionSchemaModelList(): List<com.example.budgetly.domain.models.api.banking.balance.BalanceAfterTransactionSchemaModel> =
    this.map { it.toBalanceAfterTransactionSchemaModel() }

fun List<com.example.budgetly.domain.models.api.banking.balance.BalanceAfterTransactionSchemaModel>.toBalanceAfterTransactionSchemaList(): List<BalanceAfterTransactionSchema> =
    this.map { it.toBalanceAfterTransactionSchema() }

fun Flow<List<BalanceAfterTransactionSchema>>.toBalanceAfterTransactionSchemaModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.balance.BalanceAfterTransactionSchemaModel>> =
    this.map { it.toBalanceAfterTransactionSchemaModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.balance.BalanceAfterTransactionSchemaModel>>.toBalanceAfterTransactionSchemaFlow(): Flow<List<BalanceAfterTransactionSchema>> =
    this.map { it.toBalanceAfterTransactionSchemaList() }
