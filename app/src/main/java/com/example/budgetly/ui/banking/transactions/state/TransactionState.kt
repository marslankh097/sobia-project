package com.example.budgetly.ui.banking.transactions.state

// --- TransactionState.kt ---
data class TransactionState(
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val allTransactions: List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> = emptyList(),
    val incomeTransactions: List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> = emptyList(),
    val expenseTransactions: List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> = emptyList()
)
