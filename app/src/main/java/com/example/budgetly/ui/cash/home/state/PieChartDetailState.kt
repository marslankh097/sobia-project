package com.example.budgetly.ui.cash.home.state

data class PieChartDetailState(
    val screenTitle:String = "",
    val slices: List<PieSlice> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)