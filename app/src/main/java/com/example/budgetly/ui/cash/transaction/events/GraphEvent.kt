package com.example.budgetly.ui.cash.transaction.events

sealed class GraphEvent {
    data object LoadTransactionGraph : GraphEvent()
    data object ChangeGraphType : GraphEvent()
}
