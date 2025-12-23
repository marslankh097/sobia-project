package com.example.budgetly.domain.repositories.db.cash

import com.example.budgetly.domain.models.db.transactions.AccountModel
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun insertAccount(account: AccountModel): Long
    fun getAllAccounts(): Flow<List<AccountModel>>
    suspend fun getAccountById(id: Long): AccountModel?
    suspend fun deleteAccountById(id: Long)
    suspend fun updateAccount(account: AccountModel)
    suspend fun deleteAccount(account: AccountModel)
    fun getAccountByIdFlow(accountId: Long): Flow<AccountModel?>
}
