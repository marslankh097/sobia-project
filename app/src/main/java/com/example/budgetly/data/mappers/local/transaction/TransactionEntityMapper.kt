package com.example.budgetly.data.mappers.local.transaction
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun TransactionEntity.toTransactionModel(categoryName:String, subCategoryName:String): TransactionModel {
    return TransactionModel(
        accountId = this.accountId,
        subcategoryId = this.subcategoryId,
        categoryId = this.categoryId,
        transactionId = this.transactionId,
        date = this.date,
        amount = this.amount,
        type = this.type,
        frequency = this.frequency,
        currency = this.currency,
        description = this.description,
        categoryName = categoryName,
        subCategoryName = subCategoryName,
        createdAt = createdAt,
        lastModified = lastModified
    )
}
fun TransactionEntity.toTransactionModel(): TransactionModel {
    return TransactionModel(
        accountId = this.accountId,
        subcategoryId = this.subcategoryId,
        categoryId = this.categoryId,
        transactionId = this.transactionId,
        date = this.date,
        amount = this.amount,
        type = this.type,
        frequency = this.frequency,
        currency = this.currency,
        description = this.description,
        createdAt = createdAt,
        lastModified = lastModified
    )
}
fun TransactionModel.toTransactionEntity(): TransactionEntity {
    return TransactionEntity(
        accountId = this.accountId,
        subcategoryId = this.subcategoryId,
        categoryId = this.categoryId,
        transactionId = this.transactionId,
        date = this.date,
        amount = this.amount,
        type = this.type,
        frequency = this.frequency,
        currency = this.currency,
        description = this.description,
        createdAt = createdAt,
        lastModified = lastModified
    )
}
fun List<TransactionEntity>.toTransactionModelList(): List<TransactionModel> =
    this.map { it.toTransactionModel() }

fun List<TransactionModel>.toTransactionEntityList(): List<TransactionEntity> =
    this.map { it.toTransactionEntity() }

fun Flow<List<TransactionEntity>>.toTransactionModelFlow(): Flow<List<TransactionModel>> =
    this.map { it.toTransactionModelList() }

fun Flow<List<TransactionModel>>.toTransactionEntityFlow(): Flow<List<TransactionEntity>> =
    this.map { it.toTransactionEntityList() }
