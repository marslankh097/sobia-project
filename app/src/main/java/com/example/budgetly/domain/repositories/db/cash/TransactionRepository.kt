package com.example.budgetly.domain.repositories.db.cash
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insertTransaction(transaction: TransactionModel)
    suspend fun insertTransactions(transactions: List<TransactionModel>)
    fun getAllTransactions(): Flow<List<TransactionModel>>
    suspend fun getTransactionById(id: Long): TransactionModel?
    suspend fun deleteTransactionById(id: Long)
    fun getTransactionsByAccountId(accountId: Long = 1, fromTimestamp: Long, toTimestamp: Long): Flow<List<TransactionModel>>
    fun getAllTransactionsByAccountId(accountId: Long = 1): Flow<List<TransactionModel>>
    suspend fun updateTransaction(transaction: TransactionModel)
    suspend fun deleteTransaction(transaction: TransactionModel)
}
