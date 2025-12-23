package com.example.budgetly.data.mappers.api.balance
import com.example.budgetly.data.remote.remote_models.banking.account.BalanceAmount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun BalanceAmount.toBalanceAmountModel(): com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel {
    return com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel(
        amount = this.amount,
        currency = this.currency
    )
}

fun com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel.toBalanceAmount(): BalanceAmount {
    return BalanceAmount(
        amount = this.amount,
        currency = this.currency
    )
}

fun List<BalanceAmount>.toBalanceAmountModelList(): List<com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel> =
    this.map { it.toBalanceAmountModel() }

fun List<com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel>.toBalanceAmountList(): List<BalanceAmount> =
    this.map { it.toBalanceAmount() }

fun Flow<List<BalanceAmount>>.toBalanceAmountModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel>> =
    this.map { it.toBalanceAmountModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel>>.toBalanceAmountFlow(): Flow<List<BalanceAmount>> =
    this.map { it.toBalanceAmountList() }
