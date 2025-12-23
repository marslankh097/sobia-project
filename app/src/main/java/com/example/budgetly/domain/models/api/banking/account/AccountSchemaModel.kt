package com.example.budgetly.domain.models.api.banking.account

data class AccountSchemaModel(
    val iban: String,
    val bban: String?=null,
    val pan: String? = null,
    val maskedPan: String? = null,
    val msisdn: String? = null,
    val currency: String?= null
)
