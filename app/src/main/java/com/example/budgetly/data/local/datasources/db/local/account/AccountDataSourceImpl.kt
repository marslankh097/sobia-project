package com.example.budgetly.data.local.datasources.db.local.account

import com.example.budgetly.data.local.database.dao.local.cash.AccountDao
import com.example.budgetly.data.local.database.entities.local.cash.AccountEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//@Singleton
class AccountDataSourceImpl @Inject constructor(private val dao: AccountDao) : AccountDataSource {
    override suspend fun insertAccount(account: AccountEntity) = dao.insertAccount(account)
    override  fun getAllAccounts() = dao.getAllAccounts()
    override suspend fun getAccountById(id: Long) = dao.getAccountById(id)
    override fun getAccountByIdFlow(accountId: Long): Flow<AccountEntity?>  = dao.getAccountByIdFlow(accountId)
    override suspend fun updateAccount(account: AccountEntity) = dao.updateAccount(account)
    override suspend fun deleteAccount(account: AccountEntity) = dao.deleteAccount(account)
    override suspend fun deleteAccountById(id: Long) = dao.deleteAccountById(id)
}