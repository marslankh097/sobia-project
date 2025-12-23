package com.example.budgetly.data.remote.remote_models.banking.account

data class AccountSchema(
    val iban: String,
    val bban: String?=null,
    val pan: String? = null,
    val maskedPan: String? = null,
    val msisdn: String? = null,
    val currency: String ?= null
)
