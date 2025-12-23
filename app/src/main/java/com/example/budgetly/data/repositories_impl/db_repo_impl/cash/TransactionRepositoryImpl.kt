package com.example.budgetly.data.repositories_impl.db_repo_impl.cash
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import com.example.budgetly.data.local.datasources.db.local.category.CategoryDataSource
import com.example.budgetly.data.local.datasources.db.local.subcategory.SubCategoryDataSource
import com.example.budgetly.data.local.datasources.db.local.transaction.TransactionDataSource
import com.example.budgetly.data.mappers.local.transaction.toTransactionEntity
import com.example.budgetly.data.mappers.local.transaction.toTransactionEntityList
import com.example.budgetly.data.mappers.local.transaction.toTransactionModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.repositories.db.cash.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val dataSource: TransactionDataSource,
    private val categoryDataSource: CategoryDataSource,
    private val subCategoryDataSource: SubCategoryDataSource
) : TransactionRepository {

    override suspend fun insertTransaction(transaction: TransactionModel) {
        dataSource.insertTransaction(transaction.toTransactionEntity())
    }

    override suspend fun insertTransactions(transactions: List<TransactionModel>) {
        dataSource.insertTransactions(transactions.toTransactionEntityList())
    }

    override fun getAllTransactions(): Flow<List<TransactionModel>> = flow {
        dataSource.getAllTransactions().collect { entityList ->
            val enrichedList = entityList.map {
                it.toFullTransactionModel()
            }
            emit(enrichedList)
        }
    }

    override suspend fun getTransactionById(id: Long): TransactionModel? {
        val transaction = dataSource.getTransactionById(id) ?: return null
        return transaction.toFullTransactionModel()
    }

    override suspend fun deleteTransactionById(id: Long) {
        dataSource.deleteTransactionById(id)
    }




    override fun getTransactionsByAccountId(
        accountId: Long,
        fromTimestamp: Long,
        toTimestamp: Long
    ): Flow<List<TransactionModel>> = flow {
       dataSource.getTransactionsByAccountId(accountId, fromTimestamp, toTimestamp)
           .collect{ entityList ->
          val enrichedList = entityList.map {
              it.toFullTransactionModel()
          }
          emit(enrichedList)
       }
    }
//    override fun getTransactionsByAccountId(
//        accountId: Long,
//        fromTimestamp: Long,
//        toTimestamp: Long
//    ): Flow<List<TransactionModel>>  {
//      return dataSource.getTransactionsByAccountId(accountId, fromTimestamp, toTimestamp).toTransactionModelFlow()
//    }

    override fun getAllTransactionsByAccountId(accountId: Long): Flow<List<TransactionModel>> {
        return dataSource.getAllTransactionsByAccountId(accountId)
            .map { entityList ->
                entityList.map { it.toFullTransactionModel() }
            }
    }


    override suspend fun updateTransaction(transaction: TransactionModel) {
        dataSource.updateTransaction(transaction.toTransactionEntity())
    }

    override suspend fun deleteTransaction(transaction: TransactionModel) {
        dataSource.deleteTransaction(transaction.toTransactionEntity())
    }
    private suspend fun TransactionEntity.toFullTransactionModel(): TransactionModel {
        val categoryName = categoryId?.let {
            categoryDataSource.getCategoryById(it)?.categoryName
        } ?: ""

        val subCategoryName = subcategoryId?.let {
            subCategoryDataSource.getSubCategoryById(it)?.subCategoryName
        } ?: ""
        return toTransactionModel(categoryName, subCategoryName)
    }
}
