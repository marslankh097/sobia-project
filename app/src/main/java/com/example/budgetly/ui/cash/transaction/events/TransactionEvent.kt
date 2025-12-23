package com.example.budgetly.ui.cash.transaction.events

import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel

sealed class TransactionEvent {
    data object LoadTransactions : TransactionEvent()
    data class ShowDeleteUpdateDialog(val show: Boolean) : TransactionEvent()


    data class SetDisplayAccount(val account: AccountModel?) : TransactionEvent()
    data class SetDisplayAccountById(val id:Long?) : TransactionEvent()
    data class SetDisplayCategory(val category: CategoryModel?) : TransactionEvent()
    data class SetDisplayCategoryById(val id:Long?) : TransactionEvent()
    data class SetDisplaySubCategory(val subCategory: SubCategoryModel?) : TransactionEvent()
    data class SetDisplaySubCategoryById(val id:Long?) : TransactionEvent()

    data class ShowDeleteConfirmationDialog(val show: Boolean) : TransactionEvent()
    data object DeleteTransactionById : TransactionEvent()
    data class SetDisplayCategorySubCategoryAccount(val category: CategoryModel?, val subCategory: SubCategoryModel?, val account: AccountModel?= null) : TransactionEvent()

//    data class SetTransactionDateTime(val dateTime: Long): TransactionEvent()
//    data object LoadTransactionGraph : TransactionEvent()
//    data object ChangeGraphType : TransactionEvent()
//    data class SetTransactionAmount(val amount: String) : TransactionEvent()
//    data class SetScreenSource(val source:String) : TransactionEvent()
//    data class SetTimeStamp(val fromTimeStamp:Long, val toTimeStamp:Long) : TransactionEvent()
//    data class SetTransactionCurrency(val currency: String) : TransactionEvent()
//    data class SetTransactionFrequency(val frequency: String) : TransactionEvent()
//    data class SetTransactionDuration(val transactionDuration: TransactionDuration) : TransactionEvent()
//    data class SetTransactionNotes(val notes: String) : TransactionEvent()
//    data class SetTransactionType(val type: String) : TransactionEvent()
//    data class UseCustomDateRange(val use: Boolean) : TransactionEvent()
//    data class UseAmountRange(val use: Boolean) : TransactionEvent()

//    data class SetAccount(val account: AccountModel?) : TransactionEvent()
//    data class SetCategory(val category: CategoryModel?) : TransactionEvent()
//    data class SetCategoryById(val id:Long?) : TransactionEvent()
//    data class SetSubCategory(val subCategory: SubCategoryModel?) : TransactionEvent()
//    data class SetSubCategoryById(val id:Long?) : TransactionEvent()
//    data class InsertUpdateMode(val isUpdate: Boolean) : TransactionEvent()
//    data class PrepareInsertUpdate(val transactionId:Long?= null,
//                                   val amount:String = "", val currency: String = "PKR", val notes:String = "",
//                                   val frequency:String = TransactionFrequency.OneTime.name, val dateTime: Long = System.currentTimeMillis(),
////                                   , val type: String = TransactionType.Expense.name
//    ) : TransactionEvent()

//    data class ShowFrequencySelectionDialog(val show: Boolean) : TransactionEvent()
//    data class ShowSortingDialog(val show: Boolean) : TransactionEvent()
//    data class SetSortBy(val sortBy: SortBy) : TransactionEvent()
//    data class SetOrderBy(val orderBy: OrderBy) : TransactionEvent()
//    data class ShowDurationSelectionDialog(val show: Boolean) : TransactionEvent()
//    data class SetTempCurrency(val currency: String) : TransactionEvent()
//    data class SetTempCategory(val category: CategoryModel?) : TransactionEvent()
//    data class SetTempSubCategory(val subCategory: SubCategoryModel?) : TransactionEvent()
//    data class SetTempSortBy(val sortBy: SortBy) : TransactionEvent()
//    data class SetTempOrderBy(val orderBy: OrderBy) : TransactionEvent()
//    data class SetTempFromTimeStamp(val timeStamp: Long) : TransactionEvent()
//    data class SetTempToTimeStamp(val timeStamp: Long) : TransactionEvent()
//    data object InsertOrUpdateTransaction : TransactionEvent()

//    data class DisplaySubCategorySelection(val show: Boolean) : TransactionEvent()
//    data class DisplayAccountSelection(val show: Boolean) : TransactionEvent()
//    data class DisplayCategorySelection(val show: Boolean) : TransactionEvent()
//    data class SetSelectCategory (var selectCategory:Boolean) : TransactionEvent()


//    data class ShowFromCalendarDialog(val show: Boolean) : TransactionEvent()
//    data class ShowToCalendarDialog(val show: Boolean) : TransactionEvent()
//    data class SetCategoryAndSubCategory(val category: CategoryModel?,val subCategory: SubCategoryModel?,
//                                         val displayCategorySelection: Boolean,
//                                         val displaySubCategorySelection: Boolean,
//                                         val displayAccountSelection: Boolean = false
//    ) : TransactionEvent()
}
