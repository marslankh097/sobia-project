package com.example.budgetly.data.remote.remote_models.banking.account

import com.example.budgetly.data.remote.remote_models.banking.account.TransactionSchema

data class BankTransaction(
    val booked: List<TransactionSchema>,
    val pending: List<TransactionSchema>? = null
)
