package com.example.budgetly.ui.cash.home.state

data class PieChartState(
//    val parentCategoryId: Long? = null,
//    val isSubcategoryView: Boolean = false,
    val expenseSlices: List<PieSlice> = emptyList(),
    val incomeSlices: List<PieSlice> = emptyList(),
//    val subcategorySlices: List<PieSlice> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)