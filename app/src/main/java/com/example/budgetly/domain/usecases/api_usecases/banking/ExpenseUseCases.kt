package com.example.budgetly.domain.usecases.api_usecases.banking

import com.example.budgetly.domain.repositories.api.banking.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class ExpenseUseCases @Inject constructor(
    val getExpenseTransactions: GetExpenseTransactions,
    val getTotalExpenses: GetTotalExpenses,
    val getExpensesByCategory: GetExpensesByCategory,
    val getPastDaysExpenses: GetPastDaysExpenses
)
////
@Singleton
class GetExpenseTransactions @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return repository.getExpenseTransactions(accountId, fromDate, toDate)
    }
}
@Singleton
class GetTotalExpenses @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): Double {
        return repository.getTotalExpenses(accountId, fromDate, toDate)
    }
}
@Singleton
class GetExpensesByCategory @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): Map<String, Double> {
        return repository.getExpensesByCategory(accountId, fromDate, toDate)
    }
}
@Singleton
class GetPastDaysExpenses @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return repository.getPastDaysExpenses(accountId, days)
    }
}