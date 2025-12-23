package com.example.budgetly.data.local.datasources.db.local.account

import com.example.budgetly.data.local.database.entities.local.cash.AccountEntity
import kotlinx.coroutines.flow.Flow

interface AccountDataSource {
    suspend fun insertAccount(account: AccountEntity): Long
    fun getAllAccounts(): Flow<List<AccountEntity>>
    suspend fun getAccountById(id: Long): AccountEntity?
    suspend fun updateAccount(account: AccountEntity)
    suspend fun deleteAccount(account: AccountEntity)
    suspend fun deleteAccountById(id: Long)
    fun getAccountByIdFlow(accountId: Long): Flow<AccountEntity?>
}