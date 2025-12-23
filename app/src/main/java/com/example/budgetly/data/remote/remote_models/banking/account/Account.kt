package com.example.budgetly.data.remote.remote_models.banking.account

data class Account(
    val id: String,
    val created: String,
    val last_accessed: String?= null,
    val iban: String,
    val bban: String?=null,
    val status: String,
    val institution_id: String,
    val owner_name: String,
    val name: String
)
