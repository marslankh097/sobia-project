// SubCategoryRepository.kt
package com.example.budgetly.domain.repositories.db.cash
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import kotlinx.coroutines.flow.Flow

interface SubCategoryRepository {
    fun getSubCategoriesByCategoryId(categoryId: Long): Flow<List<SubCategoryModel>>
    suspend fun insertSubCategory(subCategory: SubCategoryModel)
    suspend fun insertSubCategories(subCategories: List<SubCategoryModel>)
    suspend fun updateSubCategory(subCategory: SubCategoryModel)
    suspend fun deleteSubCategory(subCategory: SubCategoryModel)
    suspend fun getSubCategoryById(id: Long): SubCategoryModel?
    fun getTransactionsBySubCategoryId(accountId: Long, subCategoryId: Long): Flow<List<TransactionModel>>
    fun getTransactionsGroupedBySubCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionModel>>>
    suspend fun deleteSubCategoryById(id: Long)
    fun getTransactionTotalBySubCategory(accountId:Long = 1, fromTimeStamp:Long, toTimeStamp:Long): Flow<Map<Long,Pair<Double, String> >>
    fun getTransactionTotalBySubCategory(accountId:Long = 1): Flow<Map<Long,Pair<Double, String> >>
}
