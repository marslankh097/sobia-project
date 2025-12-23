package com.example.budgetly.data.repositories_impl.api_repo_impl.banking
import com.example.budgetly.data.mappers.api.transaction.toTransactionSchemaModelList
import com.example.budgetly.data.remote.datasources.retrofit.AccountDataSource
import com.example.budgetly.domain.repositories.api.banking.TransactionRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountDataSource
) : TransactionRepository {

    override suspend fun getTransactions(accountId: String, fromDate: String, toDate: String): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return remoteDataSource.getAccountTransactions(accountId).transactions.booked.filter {
            it.bookingDate?.isWithinRange(fromDate, toDate) ?: false
        }.toTransactionSchemaModelList()
    }

    override suspend fun getIncomeTransactions(accountId: String, fromDate: String, toDate: String): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return getTransactions(accountId, fromDate, toDate).filter { isIncome(it) }
    }

    override suspend fun getExpenseTransactions(accountId: String, fromDate: String, toDate: String): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return getTransactions(accountId, fromDate, toDate).filter { !isIncome(it) }
    }

    override suspend fun getTransactionsSortedByAmount(accountId: String, fromDate: String, toDate: String, ascending: Boolean): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        return getTransactions(accountId, fromDate, toDate).sortedBy { it.transactionAmount.amount.toDoubleOrNull() ?: 0.0 }.let {
            if (ascending) it else it.reversed()
        }
    }

    override suspend fun getTotalIncome(accountId: String, fromDate: String, toDate: String): Double {
        return getIncomeTransactions(accountId, fromDate, toDate).sumOf { it.transactionAmount.amount.toDoubleOrNull() ?: 0.0 }
    }

    override suspend fun getTotalExpenses(accountId: String, fromDate: String, toDate: String): Double {
        return getExpenseTransactions(accountId, fromDate, toDate).sumOf { it.transactionAmount.amount.toDoubleOrNull() ?: 0.0 }
    }

    override suspend fun getRemainingIncome(accountId: String, fromDate: String, toDate: String): Double {
        return getTotalIncome(accountId, fromDate, toDate) - getTotalExpenses(accountId, fromDate, toDate)
    }

    override suspend fun getIncomeByCategory(accountId: String, fromDate: String, toDate: String): Map<String, Double> {
        return getIncomeTransactions(accountId, fromDate, toDate)
            .groupBy { categorizeTransaction(it) }
            .mapValues { entry -> entry.value.sumOf { it.transactionAmount.amount.toDoubleOrNull() ?: 0.0 } }
    }

    override suspend fun getExpensesByCategory(accountId: String, fromDate: String, toDate: String): Map<String, Double> {
        return getExpenseTransactions(accountId, fromDate, toDate)
            .groupBy { categorizeTransaction(it) }
            .mapValues { entry -> entry.value.sumOf { it.transactionAmount.amount.toDoubleOrNull() ?: 0.0 } }
    }

    // Common Duration Methods

    override suspend fun getPastDaysTransactions(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        val (fromDate, toDate) = getDateRangeForPastDays(days)
        return getTransactions(accountId, fromDate, toDate)
    }

    override suspend fun getPastDaysIncome(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        val (fromDate, toDate) = getDateRangeForPastDays(days)
        return getIncomeTransactions(accountId, fromDate, toDate)
    }

    override suspend fun getPastDaysExpenses(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> {
        val (fromDate, toDate) = getDateRangeForPastDays(days)
        return getExpenseTransactions(accountId, fromDate, toDate)
    }

    // Helpers

    private fun isIncome(tx: com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel): Boolean {
        return tx.transactionAmount.amount.toDoubleOrNull()?.let { it > 0 } ?: false
    }

    private fun categorizeTransaction(tx: com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel): String {
        return when {
            tx.purposeCode?.contains("SALARY", ignoreCase = true) == true -> "Salary"
            tx.creditorName?.contains("GROCERY", ignoreCase = true) == true -> "Groceries"
            tx.creditorName?.contains("RENT", ignoreCase = true) == true -> "Rent"
            tx.creditorName?.contains("UTILITY", ignoreCase = true) == true -> "Utilities"
            else -> "Other"
        }
    }

    private fun getDateRangeForPastDays(days: Int): Pair<String, String> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val toDate = sdf.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        val fromDate = sdf.format(calendar.time)
        return fromDate to toDate
    }

    private fun String.isWithinRange(startDate: String, endDate: String, pattern: String = "yyyy-MM-dd"): Boolean {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val target = sdf.parse(this)
        val start = sdf.parse(startDate)
        val end = sdf.parse(endDate)
        return if (target != null && start != null && end != null) {
            !target.before(start) && !target.after(end)
        } else {
            false
        }
    }
}

