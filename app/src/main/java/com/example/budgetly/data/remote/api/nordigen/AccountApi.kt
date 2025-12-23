package com.example.budgetly.data.remote.api.nordigen
import com.example.budgetly.data.remote.remote_models.banking.account.AccountBalance
import com.example.budgetly.data.remote.remote_models.banking.account.AccountDetail
import com.example.budgetly.data.remote.remote_models.banking.account.AccountTransactions
import com.example.budgetly.data.remote.remote_models.banking.account.Account
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AccountApi {

    // region ─────── ACCOUNTS ───────

    @GET("accounts/{id}/")
    suspend fun getAccount(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Account

    @GET("accounts/{id}/balances/")
    suspend fun getAccountBalances(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AccountBalance

    @GET("accounts/{id}/details/")
    suspend fun getAccountDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AccountDetail

    @GET("accounts/{id}/transactions/")
    suspend fun getAccountTransactions(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AccountTransactions
}