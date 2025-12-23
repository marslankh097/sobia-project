package com.example.budgetly.ui.cash.accounts.state

import com.example.budgetly.domain.models.enums.account.AccountCategory
import com.example.budgetly.domain.models.enums.account.AccountType

data class AccountInsertState(
    val accountId: Long? = null,
    val accountIBAN: String = "",
    val balance: String = "",
    val currency: String = "PKR",
    val tempCurrency: String = "PKR",
    val accountType: String = AccountType.Cash.name,
    val accountCategory: String = AccountCategory.Personal.name,

    val showAccountTypeSelectionDialog: Boolean = false,
    val showAccountCategorySelectionDialog: Boolean = false,
    val showFrequencySelectionDialog: Boolean = false,

    val isUpdateMode: Boolean = false
)
