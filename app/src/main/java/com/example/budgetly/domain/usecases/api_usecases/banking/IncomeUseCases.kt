package com.example.budgetly.domain.usecases.api_usecases.banking

import com.example.budgetly.domain.repositories.api.banking.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class IncomeUseCases @Inject constructor(
    val getIncomeTransactions: GetIncomeTransactions,
    val getTotalIncome: GetTotalIncome,
    val getRemainingIncome: GetRemainingIncome,
    val getIncomeByCategory: GetIncomeByCategory,
    val getPastDaysIncome: GetPastDaysIncome
)
////
@Singleton
class GetIncomeTransactions @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return repository.getIncomeTransactions(accountId, fromDate, toDate)
    }
}
@Singleton
class GetTotalIncome @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): Double {
        return repository.getTotalIncome(accountId, fromDate, toDate)
    }
}
@Singleton
class GetRemainingIncome @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): Double {
        return repository.getRemainingIncome(accountId, fromDate, toDate)
    }
}
@Singleton
class GetIncomeByCategory @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, fromDate: String, toDate: String): Map<String, Double> {
        return repository.getIncomeByCategory(accountId, fromDate, toDate)
    }
}
@Singleton
class GetPastDaysIncome @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return repository.getPastDaysIncome(accountId, days)
    }
}