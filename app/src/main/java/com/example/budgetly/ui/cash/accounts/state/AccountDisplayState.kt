package com.example.budgetly.ui.cash.accounts.state

import com.example.budgetly.domain.models.db.transactions.AccountModel

data class AccountDisplayState(
    val accountsList: List<AccountModel> = emptyList(),
    val selectedAccount: AccountModel?= null,
    val tempAccount: AccountModel?= null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showUpdateDeleteDialog: Boolean = false,
    val showDeleteConfirmationDialog: Boolean = false
)
