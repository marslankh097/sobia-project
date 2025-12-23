package com.example.budgetly.domain.models.api.banking.transaction

data class CurrencyExchangeSchemaModel(
    val sourceCurrency: String? = null,
    val exchangeRate: String? = null,
    val unitCurrency: String? = null,
    val targetCurrency: String? = null,
    val quotationDate: String? = null,
    val contractIdentification: String? = null
)
