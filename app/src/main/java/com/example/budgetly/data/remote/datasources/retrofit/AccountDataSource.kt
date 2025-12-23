package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.remote_models.banking.account.Account
import com.example.budgetly.data.remote.remote_models.banking.account.AccountBalance
import com.example.budgetly.data.remote.remote_models.banking.account.AccountDetail
import com.example.budgetly.data.remote.remote_models.banking.account.AccountTransactions

interface AccountDataSource {
    suspend fun getAccount(id: String): Account
    suspend fun getAccountBalances(id: String): AccountBalance
    suspend fun getAccountDetails(id: String): AccountDetail
    suspend fun getAccountTransactions(id: String): AccountTransactions
}