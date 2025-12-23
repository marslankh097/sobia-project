package com.example.budgetly.ui.cash.accounts.events

import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.enums.account.AccountCategory
import com.example.budgetly.domain.models.enums.account.AccountType

sealed class AccountEvent {
    data class SetAccountIBAN(val iban: String) : AccountEvent()
    data class SetAccountBalance(val balance: String) : AccountEvent()
    data class SetAccountType(val accountType: String) : AccountEvent()
    data class SetAccountCategory(val accountCategory: String) : AccountEvent()
    data class SetTargetAccount(
        val accountId: Long = 0L,
        val accountType: String = AccountType.Cash.name,
        val accountCategory: String = AccountCategory.Personal.name,
        val currency: String = "PKR",
        val accountIBAN: String = "",
        val balance: String = "",
    ) : AccountEvent()

    data object LoadAccounts : AccountEvent()
    data class SetTempAccount(val account: AccountModel?) : AccountEvent()
    data class SetSelectedAccount(val account: AccountModel?) : AccountEvent()
    data class SetTempCurrency(val currency: String) : AccountEvent()
    data class InsertUpdateMode(val isUpdateMode: Boolean) : AccountEvent()
    data object DeleteByAccountId : AccountEvent()

    data class ShowAccountTypeSelectionDialog(val show: Boolean) : AccountEvent()
    data class ShowAccountCategorySelectionDialog(val show: Boolean) : AccountEvent()

    data class ShowDeleteUpdateDialog(val show: Boolean) : AccountEvent()
    data class ShowDeleteConfirmationDialog(val show: Boolean) : AccountEvent()

    data class InsertOrUpdateAccount(val account: AccountModel) : AccountEvent()
}
