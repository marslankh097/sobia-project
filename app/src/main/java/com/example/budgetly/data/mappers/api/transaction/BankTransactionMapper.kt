package com.example.budgetly.data.mappers.api.transaction

import com.example.budgetly.data.remote.remote_models.banking.account.BankTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun BankTransaction.toBankTransactionModel(): com.example.budgetly.domain.models.api.banking.transaction.BankTransactionModel {
    return com.example.budgetly.domain.models.api.banking.transaction.BankTransactionModel(
        booked = this.booked.map { it.toTransactionSchemaModel() },
        pending = this.pending?.map { it.toTransactionSchemaModel() }
    )
}
fun com.example.budgetly.domain.models.api.banking.transaction.BankTransactionModel.toBankTransaction(): BankTransaction {
    return BankTransaction(
        booked = this.booked.map { it.toTransactionSchema() },
        pending = this.pending?.map { it.toTransactionSchema() }
    )
}
fun List<BankTransaction>.toBankTransactionModelList(): List<com.example.budgetly.domain.models.api.banking.transaction.BankTransactionModel> =
    this.map { it.toBankTransactionModel() }

fun List<com.example.budgetly.domain.models.api.banking.transaction.BankTransactionModel>.toBankTransactionList(): List<BankTransaction> =
    this.map { it.toBankTransaction() }

fun Flow<List<BankTransaction>>.toBankTransactionModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.transaction.BankTransactionModel>> =
    this.map { it.toBankTransactionModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.transaction.BankTransactionModel>>.toBankTransactionFlow(): Flow<List<BankTransaction>> =
    this.map { it.toBankTransactionList() }