package com.example.budgetly.domain.models.api.banking.balance

data class BalanceSchemaModel(
    val balanceAmount: com.example.budgetly.domain.models.api.banking.balance.BalanceAmountModel,
    val balanceType: String,
    val creditLimitIncluded: Boolean? = null,
    val lastChangeDateTime: String? = null,
    val referenceDate: String? = null,
    val lastCommittedTransaction: String? = null
)
