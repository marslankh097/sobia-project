package com.example.budgetly.domain.repositories.api.banking

import com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel
import com.example.budgetly.domain.models.api.banking.account.AccountDetailModel
import com.example.budgetly.domain.models.api.banking.account.AccountModel
import com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel

interface AccountRepository {
    suspend fun getAccount(id: String): com.example.budgetly.domain.models.api.banking.account.AccountModel
    suspend fun getAccountBalances(id: String): com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel
    suspend fun getAccountDetails(id: String): com.example.budgetly.domain.models.api.banking.account.AccountDetailModel
    suspend fun getAccountTransactions(id: String): com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel
}
