package com.example.budgetly.data.mappers.api.account

import com.example.budgetly.data.remote.remote_models.banking.account.AccountSchema
import com.example.budgetly.domain.models.api.banking.account.AccountSchemaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AccountSchema.toAccountSchemaModel(): com.example.budgetly.domain.models.api.banking.account.AccountSchemaModel {
    return AccountSchemaModel(
        iban = this.iban,
        bban = this.bban,
        pan = this.pan,
        maskedPan = this.maskedPan,
        msisdn = this.msisdn,
        currency = this.currency
    )
}

fun com.example.budgetly.domain.models.api.banking.account.AccountSchemaModel.toAccountSchema(): AccountSchema {
    return AccountSchema(
        iban = this.iban,
        bban = this.bban,
        pan = this.pan,
        maskedPan = this.maskedPan,
        msisdn = this.msisdn,
        currency = this.currency
    )
}

fun List<AccountSchema>.toAccountSchemaModelList(): List<com.example.budgetly.domain.models.api.banking.account.AccountSchemaModel> =
    this.map { it.toAccountSchemaModel() }

fun List<com.example.budgetly.domain.models.api.banking.account.AccountSchemaModel>.toAccountSchemaList(): List<AccountSchema> =
    this.map { it.toAccountSchema() }

fun Flow<List<AccountSchema>>.toAccountSchemaModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountSchemaModel>> =
    this.map { it.toAccountSchemaModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountSchemaModel>>.toAccountSchemaFlow(): Flow<List<AccountSchema>> =
    this.map { it.toAccountSchemaList() }
