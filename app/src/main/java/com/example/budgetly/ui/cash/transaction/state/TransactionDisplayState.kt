package com.example.budgetly.ui.cash.transaction.state

import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel

data class TransactionDisplayState(
    val isLoading: Boolean = false,
//    var screenSource:String = TransactionScreenSource.All.name,
    val accountId: Long?= null,
    val categoryId: Long?= null,
    val subcategoryId: Long?= null,

    val accountModel: AccountModel? = null,
    val categoryModel: CategoryModel? = null,
    val subCategoryModel: SubCategoryModel? = null,

//    var transactionDuration: TransactionDuration = TransactionDuration.OneMonth,
//    var sortBy: SortBy = SortBy.DateCreated,
//    var orderBy: OrderBy = OrderBy.Descending,
//    var useCustomDateRange:Boolean = false,
//    var useAmountRange:Boolean = false,
//    var fromTimeStamp:Long = 0,
//    var toTimeStamp:Long = 0,

    val transactions: List<TransactionModel> = emptyList(),
    val showUpdateDeleteDialog:Boolean = false,
//    val showFromCalenderDialog:Boolean = false,
//    val showToCalenderDialog:Boolean = false,
//    val showDurationSelectionDialog:Boolean = false,
//    val showSortingDialog:Boolean = false,
    val showDeleteConfirmationDialog:Boolean = false,
    val error: String? = null
)
