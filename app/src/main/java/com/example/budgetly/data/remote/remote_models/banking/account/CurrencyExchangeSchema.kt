package com.example.budgetly.data.remote.remote_models.banking.account

data class CurrencyExchangeSchema(
    val sourceCurrency: String? = null,
    val exchangeRate: String? = null,
    val unitCurrency: String? = null,
    val targetCurrency: String? = null,
    val quotationDate: String? = null,
    val contractIdentification: String? = null
)
