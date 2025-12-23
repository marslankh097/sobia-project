package com.example.budgetly.ui.cash.category.state

import com.example.budgetly.domain.models.enums.category.CategoryUrgency

data class CategoryInsertState(
    val categoryName: String = "",
    val parentCategoryName: String = "None",
    val selectedParentCategoryId: Long? = null,
    var targetId:Long?= null,
    var isUpdateMode:Boolean = false,
    var urgency: String = CategoryUrgency.Need.name,
    var showUrgencySelectionDialog:Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
