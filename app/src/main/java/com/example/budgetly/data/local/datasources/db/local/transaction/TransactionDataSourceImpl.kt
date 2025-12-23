package com.example.budgetly.data.local.datasources.db.local.transaction

import com.example.budgetly.data.local.database.dao.local.cash.TransactionDao
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import javax.inject.Inject

//@Singleton
class TransactionDataSourceImpl @Inject constructor(private val dao: TransactionDao) : TransactionDataSource {
    override suspend fun insertTransaction(transaction: TransactionEntity) = dao.insertTransaction(transaction)
    override suspend fun insertTransactions(transactions: List<TransactionEntity>) = dao.insertTransaction(transactions)

    override  fun getAllTransactions() = dao.getAllTransactions()
    override  fun getAllTransactionsByAccountId(accountId: Long) = dao.getAllTransactionsByAccountId(accountId)
    override suspend fun getTransactionById(id: Long): TransactionEntity? = dao.getTransactionById(id)
    override suspend fun deleteTransactionById(id: Long) = dao.deleteTransactionById(id)


    override  fun getTransactionsByAccountId(
        accountId: Long,
        fromTimestamp: Long,
        toTimestamp: Long
    ) = dao.getTransactionsByAccountId(accountId,fromTimestamp,toTimestamp)
    override suspend fun updateTransaction(transaction: TransactionEntity) = dao.updateTransaction(transaction)
    override suspend fun deleteTransaction(transaction: TransactionEntity) = dao.deleteTransaction(transaction)
}