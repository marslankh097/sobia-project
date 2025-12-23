package com.example.budgetly.ui.cash.category.state

import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration

data class CategoryDisplayState(
    val accountId:Long? = null,
    val fromTimeStamp:Long? = null,
    val toTimeStamp:Long? = null,
    var transactionDuration: TransactionDuration = TransactionDuration.OneMonth,
    val expenseCategories: List<CategoryModel> = emptyList(),
    val incomeCategories: List<CategoryModel> = emptyList(),
    val subCategoriesMap: Map<Long, List<SubCategoryModel>> = emptyMap(),
    val showUpdateDeleteDialog:Boolean = false,
    val showDeleteConfirmationDialog:Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
