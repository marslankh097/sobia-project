package com.example.budgetly.data.mappers.api.account

import com.example.budgetly.data.mappers.api.balance.toBalanceSchema
import com.example.budgetly.data.mappers.api.balance.toBalanceSchemaModel
import com.example.budgetly.data.remote.remote_models.banking.account.AccountBalance
import com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AccountBalance.toAccountBalanceModel(): com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel {
    return AccountBalanceModel(
        balances = this.balances.map { it.toBalanceSchemaModel() }
    )
}

fun com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel.toAccountBalance(): AccountBalance {
    return AccountBalance(
        balances = this.balances.map { it.toBalanceSchema() }
    )
}

fun List<AccountBalance>.toAccountBalanceModelList(): List<com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel> =
    this.map { it.toAccountBalanceModel() }

fun List<com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel>.toAccountBalanceList(): List<AccountBalance> =
    this.map { it.toAccountBalance() }

fun Flow<List<AccountBalance>>.toAccountBalanceModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel>> =
    this.map { it.toAccountBalanceModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel>>.toAccountBalanceFlow(): Flow<List<AccountBalance>> =
    this.map { it.toAccountBalanceList() }
