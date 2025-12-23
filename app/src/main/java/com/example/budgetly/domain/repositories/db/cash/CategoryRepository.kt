// CategoryRepository.kt
package com.example.budgetly.domain.repositories.db.cash
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
//    fun getAllCategories(accountId: Long = 1): Flow<List<CategoryModel>>
    fun getAllCategories(): Flow<List<CategoryModel>>
    suspend fun insertCategory(category: CategoryModel):Long
    suspend fun updateCategory(category: CategoryModel)
    suspend fun deleteCategory(category: CategoryModel)
    suspend fun getCategoryById(id: Long): CategoryModel?
    fun getTransactionsByCategoryId(accountId: Long, categoryId: Long): Flow<List<TransactionModel>>
    fun getTransactionsGroupedByCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionModel>>>
    suspend fun deleteCategoryById(id: Long)
    fun getTransactionTotalByCategory(accountId: Long = 1, fromTimeStamp:Long, toTimeStamp:Long):Flow<Map<Long,Pair<Double, String> >>
    fun getTransactionTotalByCategory(accountId: Long = 1):Flow<Map<Long,Pair<Double, String> >>
}
