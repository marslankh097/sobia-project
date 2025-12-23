package com.example.budgetly.data.mappers.api.account

import com.example.budgetly.data.remote.remote_models.banking.account.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Account.toAccountModel(): com.example.budgetly.domain.models.api.banking.account.AccountModel {
    return com.example.budgetly.domain.models.api.banking.account.AccountModel(
        id = this.id,
        created = this.created,
        last_accessed = this.last_accessed,
        iban = this.iban,
        bban = this.bban,
        status = this.status,
        institution_id = this.institution_id,
        owner_name = this.owner_name,
        name = this.name
    )
}

fun com.example.budgetly.domain.models.api.banking.account.AccountModel.toAccount(): Account {
    return Account(
        id = this.id,
        created = this.created,
        last_accessed = this.last_accessed,
        iban = this.iban,
        bban = this.bban,
        status = this.status,
        institution_id = this.institution_id,
        owner_name = this.owner_name,
        name = this.name
    )
}

fun List<Account>.toAccountModelList(): List<com.example.budgetly.domain.models.api.banking.account.AccountModel> =
    this.map { it.toAccountModel() }

fun List<com.example.budgetly.domain.models.api.banking.account.AccountModel>.toAccountList(): List<Account> =
    this.map { it.toAccount() }

fun Flow<List<Account>>.toAccountModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountModel>> =
    this.map { it.toAccountModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountModel>>.toAccountFlow(): Flow<List<Account>> =
    this.map { it.toAccountList() }
