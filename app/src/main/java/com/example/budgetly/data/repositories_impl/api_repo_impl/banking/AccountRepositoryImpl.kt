package com.example.budgetly.data.repositories_impl.api_repo_impl.banking

import com.example.budgetly.data.mappers.api.account.toAccountBalanceModel
import com.example.budgetly.data.mappers.api.account.toAccountDetailModel
import com.example.budgetly.data.mappers.api.account.toAccountModel
import com.example.budgetly.data.mappers.api.transaction.toAccountTransactionsModel
import com.example.budgetly.data.remote.datasources.retrofit.AccountDataSource
import com.example.budgetly.domain.repositories.api.banking.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDataSource: AccountDataSource
) : AccountRepository {
    override suspend fun getAccount(id: String): com.example.budgetly.domain.models.api.banking.account.AccountModel = accountDataSource.getAccount(id).toAccountModel()
    override suspend fun getAccountBalances(id: String): com.example.budgetly.domain.models.api.banking.account.AccountBalanceModel = accountDataSource.getAccountBalances(id).toAccountBalanceModel()
    override suspend fun getAccountDetails(id: String): com.example.budgetly.domain.models.api.banking.account.AccountDetailModel = accountDataSource.getAccountDetails(id).toAccountDetailModel()
    override suspend fun getAccountTransactions(id: String): com.example.budgetly.domain.models.api.banking.transaction.AccountTransactionsModel = accountDataSource.getAccountTransactions(id).toAccountTransactionsModel()
}
