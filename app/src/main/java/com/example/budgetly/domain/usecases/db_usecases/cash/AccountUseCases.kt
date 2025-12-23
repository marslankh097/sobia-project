package com.example.budgetly.domain.usecases.db_usecases.cash
import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.repositories.db.cash.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
data class AccountUseCases @Inject constructor(
    val insertAccount: InsertAccountUseCase,
    val getAllAccounts: GetAllAccountsUseCase,
    val getAccountById: GetAccountByIdUseCase,
    val getAccountByIdFlow: GetAccountByIdFlowUseCase,
    val deleteAccountById: DeleteAccountByIdUseCase,
    val updateAccount: UpdateAccountUseCase,
    val deleteAccount: DeleteAccountUseCase
)

@Singleton
class DeleteAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(account: AccountModel) {
        repository.deleteAccount(account)
    }
}

@Singleton
class UpdateAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(account: AccountModel) {
        repository.updateAccount(account)
    }
}

@Singleton
class DeleteAccountByIdUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteAccountById(id)
    }
}

@Singleton
class GetAccountByIdUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(id: Long): AccountModel? {
        return repository.getAccountById(id)
    }
}
@Singleton
class GetAccountByIdFlowUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(id: Long): Flow<AccountModel?>{
        return repository.getAccountByIdFlow(id)
    }
}
@Singleton
class GetAllAccountsUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<AccountModel>> {
        return repository.getAllAccounts()
    }
}

@Singleton
class InsertAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(account: AccountModel): Long {
        return repository.insertAccount(account)
    }
}
