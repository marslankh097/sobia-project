package com.example.budgetly.data.local.datasources.db.local.category

import com.example.budgetly.data.local.database.entities.local.cash.CategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface CategoryDataSource {
    suspend fun insertCategory(category: CategoryEntity): Long
     fun getAllCategories(): Flow<List<CategoryEntity>>
    suspend fun getCategoryById(id: Long): CategoryEntity?
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategoryById(id: Long)
    suspend fun deleteCategory(category: CategoryEntity)
    fun getTransactionsGroupedByCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionEntity>>>
    fun getTransactionsByCategoryId(accountId: Long  = 1, categoryId: Long): Flow<List<TransactionEntity>>
    fun getTransactionTotalByCategory(accountId: Long = 1, fromTimeStamp: Long, toTimeStamp: Long): Flow<Map<Long,Pair<Double, String> >>
    fun getTransactionTotalByCategory(accountId: Long = 1): Flow<Map<Long,Pair<Double, String> >>
}
