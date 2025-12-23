package com.example.budgetly.domain.repositories.api.banking

interface TransactionRepository {

    suspend fun getTransactions(
        accountId: String,
        fromDate: String,
        toDate: String
    ): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>

    suspend fun getIncomeTransactions(
        accountId: String,
        fromDate: String,
        toDate: String
    ): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>

    suspend fun getExpenseTransactions(
        accountId: String,
        fromDate: String,
        toDate: String
    ): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>

    suspend fun getTransactionsSortedByAmount(
        accountId: String,
        fromDate: String,
        toDate: String,
        ascending: Boolean = true
    ): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>

    suspend fun getTotalIncome(
        accountId: String,
        fromDate: String,
        toDate: String
    ): Double

    suspend fun getTotalExpenses(
        accountId: String,
        fromDate: String,
        toDate: String
    ): Double

    suspend fun getRemainingIncome(
        accountId: String,
        fromDate: String,
        toDate: String
    ): Double

    suspend fun getIncomeByCategory(
        accountId: String,
        fromDate: String,
        toDate: String
    ): Map<String, Double>

    suspend fun getExpensesByCategory(
        accountId: String,
        fromDate: String,
        toDate: String
    ): Map<String, Double>

    // Generalized duration-based fetchers

    suspend fun getPastDaysTransactions(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>

    suspend fun getPastDaysIncome(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>

    suspend fun getPastDaysExpenses(accountId: String, days: Int): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>
}

