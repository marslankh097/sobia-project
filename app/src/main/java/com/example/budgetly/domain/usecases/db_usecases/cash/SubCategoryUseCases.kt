package com.example.budgetly.domain.usecases.db_usecases.cash

import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.repositories.db.cash.SubCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


data class SubCategoryUseCases @Inject constructor(
    val insertSubCategory: InsertSubCategoryUseCase,
    val insertSubCategories: InsertSubCategoriesUseCases,
    val updateSubCategory: UpdateSubCategoryUseCase,
    val deleteSubCategory: DeleteSubCategoryUseCase,
    val deleteSubCategoryById: DeleteSubCategoryByIdUseCase,
    val getSubCategoriesByCategoryId: GetSubCategoriesByCategoryIdUseCase,
    val getSubCategoryById: GetSubCategoryByIdUseCase,
    val getTransactionTotalBySubCategory: GetTransactionTotalBySubCategory,
    val getTransactionBySubCategory: GetTransactionsGroupedBySubCategoryUseCase,
)

class GetSubCategoryByIdUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    suspend operator fun invoke(id: Long): SubCategoryModel? {
        return repository.getSubCategoryById(id)
    }
}
class GetSubCategoriesByCategoryIdUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    operator fun invoke(categoryId: Long): Flow<List<SubCategoryModel>> {
        return repository.getSubCategoriesByCategoryId(categoryId)
    }
}

class DeleteSubCategoryByIdUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteSubCategoryById(id)
    }
}

class DeleteSubCategoryUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    suspend operator fun invoke(subCategory: SubCategoryModel) {
        repository.deleteSubCategory(subCategory)
    }
}

class UpdateSubCategoryUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    suspend operator fun invoke(subCategory: SubCategoryModel) {
        repository.updateSubCategory(subCategory)
    }
}

class InsertSubCategoryUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    suspend operator fun invoke(subCategory: SubCategoryModel) {
        repository.insertSubCategory(subCategory)
    }
}
@javax.inject.Singleton
class GetTransactionsBySubCategoryIdUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    operator fun invoke(accountId: Long = 1,subCategoryId: Long): Flow<List<TransactionModel>> {
        return repository.getTransactionsBySubCategoryId(accountId,subCategoryId)
    }
}

class InsertSubCategoriesUseCases @Inject constructor(
    private val repository: SubCategoryRepository
) {
    suspend operator fun invoke(subCategories: List<SubCategoryModel>) {
        repository.insertSubCategories(subCategories)
    }
}
class GetTransactionsGroupedBySubCategoryUseCase @Inject constructor(
    private val repository: SubCategoryRepository
) {
    operator fun invoke(accountId: Long): Flow<Map<Long, List<TransactionModel>>> {
        return repository.getTransactionsGroupedBySubCategory(accountId)
    }
}
@javax.inject.Singleton
class GetTransactionTotalBySubCategory @Inject constructor(
    private val repository: SubCategoryRepository
) {
    operator fun invoke(accountId:Long = 1, fromStamp:Long, toStamp:Long): Flow<Map<Long,Pair<Double, String> >> {
        return repository.getTransactionTotalBySubCategory(accountId, fromStamp, toStamp)
    }
}

