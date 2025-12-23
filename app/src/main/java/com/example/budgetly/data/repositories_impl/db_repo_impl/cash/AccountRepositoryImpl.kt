package com.example.budgetly.data.repositories_impl.db_repo_impl.cash
import com.example.budgetly.data.local.datasources.db.local.account.AccountDataSource
import com.example.budgetly.data.mappers.local.account.toAccountEntity
import com.example.budgetly.data.mappers.local.account.toAccountModel
import com.example.budgetly.data.mappers.local.account.toAccountModelFlow
import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.repositories.db.cash.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(private val dataSource:AccountDataSource) :
    AccountRepository {
    override suspend fun insertAccount(account: AccountModel): Long {
        return dataSource.insertAccount(account.toAccountEntity())
    }

    override fun getAllAccounts(): Flow<List<AccountModel>> =
        dataSource.getAllAccounts().toAccountModelFlow()

    override suspend fun getAccountById(id: Long): AccountModel? =
        dataSource.getAccountById(id)?.toAccountModel()

    override suspend fun deleteAccountById(id: Long) {
        dataSource.deleteAccountById(id)
    }
    override fun getAccountByIdFlow(accountId: Long): Flow<AccountModel?>  {
       return dataSource.getAccountByIdFlow(accountId).map { it?.toAccountModel() }
    }
    override suspend fun updateAccount(account: AccountModel) {
        dataSource.updateAccount(account.toAccountEntity())
    }

    override suspend fun deleteAccount(account: AccountModel) {
        dataSource.deleteAccount(account.toAccountEntity())
    }
}
