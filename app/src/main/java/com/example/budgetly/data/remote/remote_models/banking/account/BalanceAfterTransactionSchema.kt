package com.example.budgetly.data.remote.remote_models.banking.account

data class BalanceAfterTransactionSchema(
    val amount: String,
    val currency: String? = null
)
