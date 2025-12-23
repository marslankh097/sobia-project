package com.example.budgetly.domain.models.db.transactions
import com.example.budgetly.domain.models.enums.category.CategoryUrgency

data class SubCategoryModel(
    val categoryId:Long = 0L,
    val subCategoryId:Long = 0L,
    var subCategoryName:String,
    var urgency: String = CategoryUrgency.Need.name,
    var predefined:Boolean = false,
    var transactionTotal:String = "",
    var currency:String = ""

)
