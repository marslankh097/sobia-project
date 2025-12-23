package com.example.budgetly.data.local.datasources.db.local.transaction

import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionDataSource {
    suspend fun insertTransaction(transaction: TransactionEntity)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)
     fun getAllTransactions(): Flow<List<TransactionEntity>>
    suspend fun getTransactionById(id: Long): TransactionEntity?
    suspend fun deleteTransactionById(id: Long)
     fun getTransactionsByAccountId(accountId: Long, fromTimestamp: Long, toTimestamp: Long): Flow<List<TransactionEntity>>
    fun getAllTransactionsByAccountId(accountId: Long = 1): Flow<List<TransactionEntity>>
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
