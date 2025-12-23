package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.api.nordigen.AccountApi
import jakarta.inject.Inject


class AccountDataSourceImpl @Inject constructor(
    private val api: AccountApi,
    private val tokenProvider: TokenProvider
): AccountDataSource {
    override suspend fun getAccount(id: String) = api.getAccount(tokenProvider.getToken(), id)
    override suspend fun getAccountBalances(id: String) = api.getAccountBalances(tokenProvider.getToken(), id)
    override suspend fun getAccountDetails(id: String) = api.getAccountDetails(tokenProvider.getToken(), id)
    override suspend fun getAccountTransactions(id: String) = api.getAccountTransactions(tokenProvider.getToken(), id)
}
