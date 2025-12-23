package com.example.budgetly.domain.models.api.banking.account

data class AccountBalanceModel(
    val balances: List<com.example.budgetly.domain.models.api.banking.balance.BalanceSchemaModel>
)
