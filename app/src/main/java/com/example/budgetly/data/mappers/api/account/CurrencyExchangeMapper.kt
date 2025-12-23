package com.example.budgetly.data.mappers.api.account
import com.example.budgetly.data.remote.remote_models.banking.account.CurrencyExchangeSchema
import com.example.budgetly.domain.models.api.banking.transaction.CurrencyExchangeSchemaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun CurrencyExchangeSchema.toCurrencyExchangeSchemaModel(): com.example.budgetly.domain.models.api.banking.transaction.CurrencyExchangeSchemaModel {
    return CurrencyExchangeSchemaModel(
        sourceCurrency = this.sourceCurrency,
        exchangeRate = this.exchangeRate,
        unitCurrency = this.unitCurrency,
        targetCurrency = this.targetCurrency,
        quotationDate = this.quotationDate,
        contractIdentification = this.contractIdentification
    )
}

fun com.example.budgetly.domain.models.api.banking.transaction.CurrencyExchangeSchemaModel.toCurrencyExchangeSchema(): CurrencyExchangeSchema {
    return CurrencyExchangeSchema(
        sourceCurrency = this.sourceCurrency,
        exchangeRate = this.exchangeRate,
        unitCurrency = this.unitCurrency,
        targetCurrency = this.targetCurrency,
        quotationDate = this.quotationDate,
        contractIdentification = this.contractIdentification
    )
}

fun List<CurrencyExchangeSchema>.toCurrencyExchangeSchemaModelList(): List<com.example.budgetly.domain.models.api.banking.transaction.CurrencyExchangeSchemaModel> =
    this.map { it.toCurrencyExchangeSchemaModel() }

fun List<com.example.budgetly.domain.models.api.banking.transaction.CurrencyExchangeSchemaModel>.toCurrencyExchangeSchemaList(): List<CurrencyExchangeSchema> =
    this.map { it.toCurrencyExchangeSchema() }

fun Flow<List<CurrencyExchangeSchema>>.toCurrencyExchangeSchemaModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.transaction.CurrencyExchangeSchemaModel>> =
    this.map { it.toCurrencyExchangeSchemaModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.transaction.CurrencyExchangeSchemaModel>>.toCurrencyExchangeSchemaFlow(): Flow<List<CurrencyExchangeSchema>> =
    this.map { it.toCurrencyExchangeSchemaList() }
