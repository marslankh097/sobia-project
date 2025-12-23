package com.example.budgetly.ui.cash.transaction.state

import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer

data class TransactionGraphState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val graphPoints: List<Pair<String, Double>> = emptyList(),
    val isBar: Boolean = true,
    val barModelProducer: CartesianChartModelProducer = CartesianChartModelProducer(),
    val lineModelProducer: CartesianChartModelProducer = CartesianChartModelProducer(),
)


