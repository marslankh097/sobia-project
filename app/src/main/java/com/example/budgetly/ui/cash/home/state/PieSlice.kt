package com.example.budgetly.ui.cash.home.state

import androidx.compose.ui.graphics.Color

data class PieSlice(
    val categoryId: Long,
    val label: String,
    val amount: Double,
    val proportion: Float,
    val color: Color // optional color per category
)