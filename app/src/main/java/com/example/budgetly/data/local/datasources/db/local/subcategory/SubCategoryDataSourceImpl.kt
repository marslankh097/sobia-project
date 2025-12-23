package com.example.budgetly.data.local.datasources.db.local.subcategory

import com.example.budgetly.data.local.database.dao.local.cash.SubCategoryDao
import com.example.budgetly.data.local.database.entities.local.cash.SubCategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//@Singleton
class SubCategoryDataSourceImpl @Inject constructor(private val dao: SubCategoryDao) : SubCategoryDataSource {
    override suspend fun insertSubCategory(subCategory: SubCategoryEntity) = dao.insertSubCategory(subCategory)
    override suspend fun insertSubCategories(subCategories: List<SubCategoryEntity>) = dao.insertSubCategories(subCategories)
    override  fun getAllSubCategories(): Flow<List<SubCategoryEntity>> = dao.getAllSubCategories()
    override suspend fun getSubCategoryById(id: Long) = dao.getSubCategoryById(id)
    override suspend fun deleteSubCategoryById(id: Long) =  dao.deleteSubCategoryById(id)

    override  fun getSubCategoriesByCategoryId(categoryId: Long): Flow<List<SubCategoryEntity>> = dao.getSubCategoriesByCategoryId(categoryId)
    override suspend fun updateSubCategory(subCategory: SubCategoryEntity) =  dao.updateSubCategory(subCategory)

    override suspend fun deleteSubCategory(subCategory: SubCategoryEntity) = dao.deleteSubCategory(subCategory)
    override fun getTransactionTotalBySubCategory(accountId:Long,fromTimeStamp: Long,
                                                  toTimeStamp: Long): Flow<Map<Long,Pair<Double, String>>> {
        return dao.getTransactionTotalBySubCategory(accountId, fromTimeStamp, toTimeStamp).map { list ->
            list.associate { it.subcategoryId to Pair(it.total, it.currency) }
        }
    }
    override fun getTransactionsGroupedBySubCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionEntity>>> {
        return dao.getAllTransactionsByAccountId(accountId).map { list ->
            list.groupBy { it.subcategoryId ?: 0L }
        }
    }
    override  fun getTransactionsBySubCategoryId(accountId: Long, subCategoryId: Long) = dao.getTransactionsBySubCategoryId(accountId, subCategoryId)
    override fun getTransactionTotalBySubCategory(accountId: Long): Flow<Map<Long, Pair<Double, String>>> {
        return dao.getTransactionTotalBySubCategory(accountId).map { list ->
            list.associate { it.subcategoryId to Pair(it.total, it.currency) }
        }
    }
}