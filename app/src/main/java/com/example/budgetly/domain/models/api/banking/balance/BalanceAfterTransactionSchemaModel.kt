package com.example.budgetly.domain.models.api.banking.balance

data class BalanceAfterTransactionSchemaModel(
    val amount: String,
    val currency: String? = null
)
