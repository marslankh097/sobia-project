package com.example.budgetly.data.remote.remote_models.banking.account

data class BalanceSchema(
    val balanceAmount: BalanceAmount,
    val balanceType: String,
    val creditLimitIncluded: Boolean? = null,
    val lastChangeDateTime: String? = null,
    val referenceDate: String? = null,
    val lastCommittedTransaction: String? = null
)
