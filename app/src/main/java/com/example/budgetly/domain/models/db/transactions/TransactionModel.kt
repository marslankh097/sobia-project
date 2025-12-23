package com.example.budgetly.domain.models.db.transactions

import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency
import com.example.budgetly.domain.models.enums.transaction.TransactionType

data class TransactionModel(
    var accountId:Long?,
    var subcategoryId:Long?,
    var categoryId:Long?,
    val transactionId:Long,
    val date: Long = System.currentTimeMillis(),
    val amount: String,
    var type:String  = TransactionType.Expense.name,
    var frequency:String  = TransactionFrequency.OneTime.name,
    val currency: String,
    val description: String?,
    var categoryName:String = "",
    var subCategoryName:String= "",
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)