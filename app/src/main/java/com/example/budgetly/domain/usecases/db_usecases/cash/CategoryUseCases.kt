package com.example.budgetly.domain.usecases.db_usecases.cash

import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.repositories.db.cash.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
data class CategoryUseCases @Inject constructor(
    val getAllCategories: GetAllCategoriesUseCase,
    val getTransactionTotalByCategory: GetTransactionTotalByCategory,
    val insertCategory: InsertCategoryUseCase,
    val updateCategory: UpdateCategoryUseCase,
    val deleteCategory: DeleteCategoryUseCase,
    val getCategoryById: GetCategoryByIdUseCase,
    val deleteCategoryById: DeleteCategoryByIdUseCase,
    val getTransactionByCategory: GetTransactionsGroupedByCategoryUseCase,
    )
@Singleton
class GetCategoryByIdUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(id: Long): CategoryModel? {
        return repository.getCategoryById(id)
    }
}
@Singleton
class DeleteCategoryByIdUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(id: Long) {
        return repository.deleteCategoryById(id)
    }
}

@Singleton
class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: CategoryModel) {
        repository.deleteCategory(category)
    }
}

@Singleton
class UpdateCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: CategoryModel) {
        repository.updateCategory(category)
    }
}

@Singleton
class InsertCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: CategoryModel):Long{
       return repository.insertCategory(category)
    }
}
@Singleton
class GetAllCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(): Flow<List<CategoryModel>> {
        return repository.getAllCategories()
    }
}

class GetTransactionsGroupedByCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(accountId: Long): Flow<Map<Long, List<TransactionModel>>> {
        return repository.getTransactionsGroupedByCategory(accountId)
    }
}

@Singleton
class GetTransactionTotalByCategory @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(accountId:Long = 1, fromTimeStamp:Long, toTimeStamp:Long): Flow<Map<Long,Pair<Double, String> >> {
        return repository.getTransactionTotalByCategory(accountId, fromTimeStamp, toTimeStamp)
    }
}
