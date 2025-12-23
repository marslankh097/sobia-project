package com.example.budgetly.ui.cash.transaction.events

import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency

sealed class TransactionInsertEvent {
    data class SetTransactionAmount(val amount: String) : TransactionInsertEvent()
    data class SetTransactionDateTime(val dateTime: Long): TransactionInsertEvent()
    data class SetTransactionCurrency(val currency: String) : TransactionInsertEvent()
    data class SetTransactionFrequency(val frequency: String) : TransactionInsertEvent()
    data class SetTransactionNotes(val notes: String) : TransactionInsertEvent()
    data class SetTransactionType(val type: String) : TransactionInsertEvent()
    data class SetAccount(val account: AccountModel?) : TransactionInsertEvent()
    data class SetCategory(val category: CategoryModel?) : TransactionInsertEvent()
    data class SetCategoryById(val id:Long?) : TransactionInsertEvent()
    data class SetSubCategory(val subCategory: SubCategoryModel?) : TransactionInsertEvent()
    data class SetSubCategoryById(val id:Long?) : TransactionInsertEvent()
    data class SetAccountById(val id:Long?) : TransactionInsertEvent()
    data class InsertUpdateMode(val isUpdate: Boolean) : TransactionInsertEvent()
    data class PrepareInsertUpdate(val transactionId:Long?= null, val amount:String = "", val currency: String = "PKR", val notes:String = "",
                                   val frequency:String = TransactionFrequency.OneTime.name, val dateTime: Long = System.currentTimeMillis()
    ) : TransactionInsertEvent()
    data class ShowFrequencySelectionDialog(val show: Boolean) : TransactionInsertEvent()
    data class SetTempCurrency(val currency: String) : TransactionInsertEvent()
    data class SetTempAccount(val account: AccountModel?) : TransactionInsertEvent()
    data class SetTempCategory(val category: CategoryModel?) : TransactionInsertEvent()
    data class SetTempSubCategory(val subCategory: SubCategoryModel?) : TransactionInsertEvent()
    data object InsertOrUpdateTransaction : TransactionInsertEvent()
    data class DisplaySubCategorySelection(val show: Boolean) : TransactionInsertEvent()
    data class DisplayAccountSelection(val show: Boolean) : TransactionInsertEvent()
    data class DisplayCategorySelection(val show: Boolean) : TransactionInsertEvent()
    data class SetSelectCategory (var selectCategory:Boolean) : TransactionInsertEvent()
    data class SetCategorySubCategoryAccount(val category: CategoryModel?, val subCategory: SubCategoryModel?, val account: AccountModel?= null) : TransactionInsertEvent()
    data class ShowCategorySubCategoryAccount(val displayCategorySelection: Boolean, val displaySubCategorySelection: Boolean,val displayAccountSelection: Boolean = false) : TransactionInsertEvent()
}
