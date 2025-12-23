package com.example.budgetly.ui.cash.transaction.state

import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency
import com.example.budgetly.domain.models.enums.transaction.TransactionType

data class TransactionInsertState(

    var selectCategory:Boolean = false,
    var displayCategorySelection:Boolean = false,
    var displaySubCategorySelection:Boolean = false,
    var displayAccountSelection:Boolean = false,

    val accountId: Long?= null,
    val categoryId: Long?= null,
    val subcategoryId: Long?= null,
    val accountModel: AccountModel? = null,
    val categoryModel: CategoryModel? = null,
    val subCategoryModel: SubCategoryModel? = null,

    val tempCategory: CategoryModel? = null,
    val tempAccount: AccountModel? = null,
    val tempSubCategory: SubCategoryModel? = null,
    val tempCurrency: String = "PKR",


    val transactionId: Long?= null,
    val amount: String = "",
    val currency: String = "PKR",
    val frequency: String = TransactionFrequency.OneTime.name,
    val notes: String = "",
    val type: String = TransactionType.Expense.name,
    val showFrequencySelectionDialog: Boolean = false,
    val isUpdateMode: Boolean = false,
    val dateTime: Long = System.currentTimeMillis()
)
