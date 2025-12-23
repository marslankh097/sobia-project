package com.example.budgetly.data.local.datasources.db.local.category

import com.example.budgetly.data.local.database.dao.local.cash.CategoryDao
import com.example.budgetly.data.local.database.entities.local.cash.CategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//@Singleton
class CategoryDataSourceImpl @Inject constructor(private val dao: CategoryDao) : CategoryDataSource {
    override suspend fun insertCategory(category: CategoryEntity):Long = dao.insertCategory(category)
    override  fun getAllCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()
    override suspend fun getCategoryById(id: Long) = dao.getCategoryById(id)
    override suspend fun updateCategory(category: CategoryEntity)  = dao.updateCategory(category)

    override fun getTransactionsGroupedByCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionEntity>>> {
        return dao.getAllTransactionsByAccountId(accountId).map { list ->
            list.groupBy { it.categoryId ?: 0L }
        }
    }


    override suspend fun deleteCategoryById(id: Long) = dao.deleteCategoryById(id)
    override suspend fun deleteCategory(category: CategoryEntity) = dao.deleteCategory(category)
    override  fun getTransactionsByCategoryId(accountId: Long, categoryId: Long) = dao.getTransactionsByCategoryId(accountId, categoryId)

    override fun getTransactionTotalByCategory(
        accountId: Long,
        fromTimeStamp: Long,
        toTimeStamp: Long
    ): Flow<Map<Long,Pair<Double, String> >> {
        return dao.getTransactionTotalByCategory(accountId, fromTimeStamp, toTimeStamp).map { list ->
            list.associate { it.categoryId to Pair(it.total, it.currency) }
        }
    }
    override fun getTransactionTotalByCategory(
        accountId: Long
    ): Flow<Map<Long,Pair<Double, String> >> {
        return dao.getTransactionTotalByCategory(accountId).map { list ->
            list.associate { it.categoryId to Pair(it.total, it.currency) }
        }
    }

}