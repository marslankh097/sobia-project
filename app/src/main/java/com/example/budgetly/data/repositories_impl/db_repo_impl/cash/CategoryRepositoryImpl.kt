// CategoryRepositoryImpl.kt
package com.example.budgetly.data.repositories_impl.db_repo_impl.cash
import com.example.budgetly.data.local.datasources.db.local.category.CategoryDataSource
import com.example.budgetly.data.mappers.local.category.toCategoryEntity
import com.example.budgetly.data.mappers.local.category.toCategoryModel
import com.example.budgetly.data.mappers.local.category.toCategoryModelFlow
import com.example.budgetly.data.mappers.local.transaction.toTransactionModelFlow
import com.example.budgetly.data.mappers.local.transaction.toTransactionModelList
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.repositories.db.cash.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val dataSource: CategoryDataSource
) : CategoryRepository {
//    override fun getAllCategories(accountId: Long): Flow<List<CategoryModel>> {
//        return combine(
//            dataSource.getAllCategories(),
//            dataSource.getTransactionTotalByCategory(accountId)
//        ) { categoryEntities, totalsMap ->
//            categoryEntities.map { category ->
//                val pair = totalsMap[category.categoryId]
//                category.toCategoryModel().copy(
//                    currency = pair?.second ?: "PKR",
//                    transactionTotal = pair?.first?.toString() ?: "0.0"
//                )
//            }
//        }
//    }
     override fun getAllCategories(): Flow<List<CategoryModel>> {
        return dataSource.getAllCategories().toCategoryModelFlow()
    }
    override suspend fun insertCategory(category: CategoryModel):Long {
        return dataSource.insertCategory(category.toCategoryEntity())
    }

    override suspend fun updateCategory(category: CategoryModel) {
        dataSource.updateCategory(category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: CategoryModel) {
        dataSource.deleteCategory(category.toCategoryEntity())
    }
    override fun getTransactionsByCategoryId(accountId: Long, categoryId: Long): Flow<List<TransactionModel>> {
       return  dataSource.getTransactionsByCategoryId(accountId,categoryId).toTransactionModelFlow()
    }
    // Repository
    override fun getTransactionsGroupedByCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionModel>>> {
        return dataSource.getTransactionsGroupedByCategory(accountId)
            .map { entityMap ->
                entityMap.mapValues { (_, entityList) ->
                    entityList.toTransactionModelList()
                }
            }
    }

    override suspend fun getCategoryById(id: Long): CategoryModel? =
        dataSource.getCategoryById(id)?.toCategoryModel()

    override suspend fun deleteCategoryById(id: Long) {
        dataSource.deleteCategoryById(id)
    }
    override fun getTransactionTotalByCategory(
        accountId: Long,
    ): Flow<Map<Long, Pair<Double, String>>> {
        return dataSource.getTransactionTotalByCategory(accountId)
    }
    override fun getTransactionTotalByCategory(
        accountId: Long,
        fromTimeStamp: Long,
        toTimeStamp: Long
    ): Flow<Map<Long, Pair<Double, String>>> {
        return dataSource.getTransactionTotalByCategory(accountId, fromTimeStamp, toTimeStamp)
    }
}
