package com.example.budgetly.data.mappers.local.account
import com.example.budgetly.data.local.database.entities.local.cash.AccountEntity
import com.example.budgetly.domain.models.db.transactions.AccountModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Single entity → model
fun AccountEntity.toAccountModel(): AccountModel {
    return AccountModel(
        accountId = this.accountId,
        accountIBAN = this.accountIBAN,
        accountType = this.accountTyp,
        accountCategory = this.accountCategory,
        balance = this.balance,
        initialBalance = this.initialBalance,
        currency = this.currency
    )
}

// Single model → entity
fun AccountModel.toAccountEntity(): AccountEntity {
    return AccountEntity(
        accountId = this.accountId,
        accountIBAN = this.accountIBAN,
        accountTyp = this.accountType,
        accountCategory = this.accountCategory,
        balance = this.balance,
        initialBalance = this.initialBalance,
        currency = this.currency
    )
}

// List conversions
fun List<AccountEntity>.toAccountModelList(): List<AccountModel> =
    this.map { it.toAccountModel() }

fun List<AccountModel>.toAccountEntityList(): List<AccountEntity> =
    this.map { it.toAccountEntity() }

// Flow conversions
fun Flow<List<AccountEntity>>.toAccountModelFlow(): Flow<List<AccountModel>> =
    this.map { it.toAccountModelList() }

fun Flow<List<AccountModel>>.toAccountEntityFlow(): Flow<List<AccountEntity>> =
    this.map { it.toAccountEntityList() }
