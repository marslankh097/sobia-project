// SubCategoryRepositoryImpl.kt
package com.example.budgetly.data.repositories_impl.db_repo_impl.cash
import com.example.budgetly.data.local.datasources.db.local.subcategory.SubCategoryDataSource
import com.example.budgetly.data.mappers.local.subcategory.toSubCategoryEntity
import com.example.budgetly.data.mappers.local.subcategory.toSubCategoryEntityList
import com.example.budgetly.data.mappers.local.subcategory.toSubCategoryModel
import com.example.budgetly.data.mappers.local.subcategory.toSubCategoryModelFlow
import com.example.budgetly.data.mappers.local.transaction.toTransactionModelFlow
import com.example.budgetly.data.mappers.local.transaction.toTransactionModelList
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.repositories.db.cash.SubCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(private val dataSource: SubCategoryDataSource) :
    SubCategoryRepository {
//    override fun getSubCategoriesByCategoryId(categoryId: Long,accountId:Long, fromTimeStamp: Long,
//                                              toTimeStamp: Long): Flow<List<SubCategoryModel>> {
//        return combine(
//            dataSource.getSubCategoriesByCategoryId(categoryId),
//            dataSource.getTransactionTotalBySubCategory(accountId, fromTimeStamp, toTimeStamp)
//        ) { subCategoryEntities, totalsMap ->
//            subCategoryEntities.map { sub ->
//                val pair = totalsMap[sub.subCategoryId]
//                sub.toSubCategoryModel().copy(
//                    currency = pair?.second ?: "PKR",
//                    transactionTotal = pair?.first?.toString() ?: "0.0"
//                )
//            }
//        }
//    }

    override fun getSubCategoriesByCategoryId(
        categoryId: Long
    ): Flow<List<SubCategoryModel>> {
        return dataSource.getSubCategoriesByCategoryId(categoryId).toSubCategoryModelFlow()
    }

    override suspend fun insertSubCategory(subCategory: SubCategoryModel) {
        dataSource.insertSubCategory(subCategory.toSubCategoryEntity())
    }

    override suspend fun insertSubCategories(subCategories: List<SubCategoryModel>) {
        dataSource.insertSubCategories(subCategories.toSubCategoryEntityList())
    }

    override suspend fun updateSubCategory(subCategory: SubCategoryModel) {
        dataSource.updateSubCategory(subCategory.toSubCategoryEntity())
    }

    override suspend fun deleteSubCategory(subCategory: SubCategoryModel) {
        dataSource.deleteSubCategory(subCategory.toSubCategoryEntity())
    }

    override suspend fun getSubCategoryById(id: Long): SubCategoryModel? =
        dataSource.getSubCategoryById(id)?.toSubCategoryModel()

    override suspend fun deleteSubCategoryById(id: Long) {
        dataSource.deleteSubCategoryById(id)
    }
    override fun getTransactionsBySubCategoryId(accountId: Long, subCategoryId: Long): Flow<List<TransactionModel>> {
        return dataSource.getTransactionsBySubCategoryId(accountId,subCategoryId).toTransactionModelFlow()
    }

    // Repository
    override fun getTransactionsGroupedBySubCategory(
        accountId: Long
    ): Flow<Map<Long, List<TransactionModel>>> {
        return dataSource.getTransactionsGroupedBySubCategory(accountId)
            .map { entityMap ->
                entityMap.mapValues { (_, entityList) ->
                    entityList.toTransactionModelList()
                }
            }
    }



    override fun getTransactionTotalBySubCategory(
        accountId: Long,
        fromTimeStamp: Long,
        toTimeStamp: Long
    ): Flow<Map<Long, Pair<Double, String>>> {
       return dataSource.getTransactionTotalBySubCategory(accountId, fromTimeStamp,toTimeStamp)
    }
    override fun getTransactionTotalBySubCategory(
        accountId: Long,
    ): Flow<Map<Long, Pair<Double, String>>> {
       return dataSource.getTransactionTotalBySubCategory(accountId)
    }

//    @Singleton
//    class GetTransactionTotalByCategory @Inject constructor(
//        private val repository: CategoryRepository
//    ) {
//        operator fun invoke(accountId:Long = 1, fromTimeStamp:Long, toTimeStamp:Long): Flow<Map<Long,Pair<Double, String> >> {
//            return repository.getTransactionTotalByCategory(accountId, fromTimeStamp, toTimeStamp)
//        }
//    }
}
