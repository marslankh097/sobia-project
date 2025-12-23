package com.example.budgetly.domain.usecases.db_usecases.cash

import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.repositories.db.cash.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class TransactionUseCases @Inject constructor(
    val insertTransaction: InsertTransactionUseCase,
    val insertTransactions: InsertTransactionsUseCase,
    val getAllTransactions: GetAllTransactionsUseCase,
    val getTransactionById: GetTransactionByIdUseCase,
    val deleteTransactionById: DeleteTransactionByIdUseCase,
    val getTransactionsBySubCategoryId: GetTransactionsBySubCategoryIdUseCase,
    val getTransactionsByAccountId: GetTransactionsByAccountIdUseCase,
    val getAllTransactionsByAccountId: GetAllTransactionsByAccountIdUseCase,
    val updateTransaction: UpdateTransactionUseCase,
    val deleteTransaction: DeleteTransactionUseCase
)

@Singleton
class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: TransactionModel) {
        repository.deleteTransaction(transaction)
    }
}

@Singleton
class UpdateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: TransactionModel) {
        repository.updateTransaction(transaction)
    }
}
@Singleton
class GetTransactionsByAccountIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(accountId: Long = 1,fromTimestamp: Long, toTimestamp: Long): Flow<List<TransactionModel>> {
        return repository.getTransactionsByAccountId(accountId, fromTimestamp, toTimestamp)
    }
}




@Singleton
class DeleteTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteTransactionById(id)
    }
}

@Singleton
class GetTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): TransactionModel? {
        return repository.getTransactionById(id)
    }
}

@Singleton
class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<TransactionModel>> {
        return repository.getAllTransactions()
    }
}
@Singleton
class GetAllTransactionsByAccountIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(accountId:Long = 1): Flow<List<TransactionModel>> {
        return repository.getAllTransactionsByAccountId(accountId)
    }
}

@Singleton
class InsertTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactions: List<TransactionModel>) {
        repository.insertTransactions(transactions)
    }
}

@Singleton
class InsertTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: TransactionModel) {
        repository.insertTransaction(transaction)
    }
}
