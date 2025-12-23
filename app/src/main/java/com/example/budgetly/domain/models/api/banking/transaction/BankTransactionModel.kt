package com.example.budgetly.domain.models.api.banking.transaction

data class BankTransactionModel(
    val booked: List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>,
    val pending: List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>? = null
)
