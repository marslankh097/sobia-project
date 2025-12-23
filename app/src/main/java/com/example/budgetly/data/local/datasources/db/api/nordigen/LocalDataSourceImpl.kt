package com.example.budgetly.data.local.datasources.db.api.nordigen

import com.example.budgetly.data.local.database.dao.local.cash.TransactionDao
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import com.example.budgetly.data.local.datasources.data_store.PreferenceDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// ✅ LocalDataSource Implementation
class LocalDataSourceImpl @Inject constructor(private val dao: TransactionDao, private val dataStore: PreferenceDataStore) :
    LocalDataSource {

    //banking api related transactions
    override suspend fun insertTransactions(transactions: List<TransactionEntity>) =
        dao.insertTransaction(transactions)

    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return dao.getAllTransactions()
    }

    override fun getIncomeTransactions(): Flow<List<TransactionEntity>> {
//       return dao.getTransactionsByType(TransactionType.Income.name)
       return dao.getAllTransactions()
    }

    override fun getExpenseTransactions(): Flow<List<TransactionEntity>> {
//        return dao.getTransactionsByType(TransactionType.Expense.name)
        return dao.getAllTransactions()
    }

}