package com.example.budgetly.data.mappers.api.balance

import com.example.budgetly.data.remote.remote_models.banking.account.BalanceSchema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun BalanceSchema.toBalanceSchemaModel(): com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel {
    return com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel(
        balanceAmount = this.balanceAmount.toBalanceAmountModel(),
        balanceType = this.balanceType,
        creditLimitIncluded = this.creditLimitIncluded,
        lastChangeDateTime = this.lastChangeDateTime,
        referenceDate = this.referenceDate,
        lastCommittedTransaction = this.lastCommittedTransaction
    )
}

fun com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel.toBalanceSchema(): BalanceSchema {
    return BalanceSchema(
        balanceAmount = this.balanceAmount.toBalanceAmount(),
        balanceType = this.balanceType,
        creditLimitIncluded = this.creditLimitIncluded,
        lastChangeDateTime = this.lastChangeDateTime,
        referenceDate = this.referenceDate,
        lastCommittedTransaction = this.lastCommittedTransaction
    )
}

fun List<BalanceSchema>.toBalanceSchemaModelList(): List<com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel> =
    this.map { it.toBalanceSchemaModel() }

fun List<com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel>.toBalanceSchemaList(): List<BalanceSchema> =
    this.map { it.toBalanceSchema() }

fun Flow<List<BalanceSchema>>.toBalanceSchemaModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel>> =
    this.map { it.toBalanceSchemaModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel>>.toBalanceSchemaFlow(): Flow<List<BalanceSchema>> =
    this.map { it.toBalanceSchemaList() }
