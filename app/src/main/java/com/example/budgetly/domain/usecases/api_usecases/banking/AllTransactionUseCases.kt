package com.example.budgetly.domain.usecases.api_usecases.banking

import com.example.budgetly.domain.repositories.api.banking.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class AllTransactionUseCases @Inject constructor(
    val getTransactions: GetTransactions,
    val getTransactionsSortedByAmount: GetTransactionsSortedByAmount,
    val getPastDaysTransactions: GetPastDaysTransactions
)
@Singleton
class GetTransactions @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return repository.getTransactions(accountId, fromDate, toDate)
    }
}
@Singleton
class GetTransactionsSortedByAmount @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String, ascending: Boolean): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return repository.getTransactionsSortedByAmount(accountId, fromDate, toDate, ascending)
    }
}
@Singleton
class GetPastDaysTransactions @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return repository.getPastDaysTransactions(accountId, days)
    }
}