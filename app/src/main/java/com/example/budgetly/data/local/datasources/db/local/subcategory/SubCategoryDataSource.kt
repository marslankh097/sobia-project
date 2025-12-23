package com.example.budgetly.data.local.datasources.db.local.subcategory

import com.example.budgetly.data.local.database.entities.local.cash.SubCategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface SubCategoryDataSource {
    suspend fun insertSubCategory(subCategory: SubCategoryEntity): Long
    suspend fun insertSubCategories(subCategories: List<SubCategoryEntity>)
     fun getAllSubCategories(): Flow<List<SubCategoryEntity>>
    suspend fun getSubCategoryById(id: Long): SubCategoryEntity?
    suspend fun deleteSubCategoryById(id: Long)
     fun getSubCategoriesByCategoryId(categoryId: Long): Flow<List<SubCategoryEntity>>
    fun getTransactionsBySubCategoryId(accountId: Long = 1, subCategoryId: Long): Flow<List<TransactionEntity>>
    fun getTransactionsGroupedBySubCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionEntity>>>
    suspend fun updateSubCategory(subCategory: SubCategoryEntity)
    suspend fun deleteSubCategory(subCategory: SubCategoryEntity)
    fun getTransactionTotalBySubCategory(accountId:Long = 1L, fromTimeStamp: Long,
                                         toTimeStamp: Long): Flow<Map<Long,Pair<Double, String> >>
    fun getTransactionTotalBySubCategory(accountId:Long = 1L): Flow<Map<Long,Pair<Double, String> >>
}