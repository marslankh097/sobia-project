package com.example.budgetly.domain.models.db.transactions

import com.example.budgetly.domain.models.enums.category.CategoryType

data class CategoryModel(
    val categoryId:Long = 0L, //primary key autogenerate
    var categoryName:String,
    var categoryType:String = CategoryType.Expense.name,
    var predefined:Boolean = false,
    var transactionTotal:String = "",
    var currency:String = ""
)
