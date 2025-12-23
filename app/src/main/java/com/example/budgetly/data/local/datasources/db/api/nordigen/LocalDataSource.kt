package com.example.budgetly.data.local.datasources.db.api.nordigen

import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    //banking api related
    suspend fun insertTransactions(transactions: List<TransactionEntity>)
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    fun getIncomeTransactions(): Flow<List<TransactionEntity>>
    fun getExpenseTransactions(): Flow<List<TransactionEntity>>
}