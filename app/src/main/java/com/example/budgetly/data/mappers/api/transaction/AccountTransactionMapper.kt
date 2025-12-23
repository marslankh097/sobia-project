package com.example.budgetly.data.mappers.api.transaction
import com.example.budgetly.data.remote.remote_models.banking.account.AccountTransactions
import com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AccountTransactions.toAccountTransactionsModel(): com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel {
    return AccountTransactionsModel(
        transactions = transactions.toBankTransactionModel()
    )
}
fun com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel.toAccountTransactions(): AccountTransactions {
    return AccountTransactions(
        transactions = transactions.toBankTransaction()
    )
}
fun List<AccountTransactions>.toAccountTransactionsModelList(): List<com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel> =
    this.map { it.toAccountTransactionsModel() }

fun List<com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel>.toAccountTransactionsList(): List<AccountTransactions> =
    this.map { it.toAccountTransactions() }

fun Flow<List<AccountTransactions>>.toAccountTransactionsModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel>> =
    this.map { it.toAccountTransactionsModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel>>.toAccountTransactionsFlow(): Flow<List<AccountTransactions>> =
    this.map { it.toAccountTransactionsList() }