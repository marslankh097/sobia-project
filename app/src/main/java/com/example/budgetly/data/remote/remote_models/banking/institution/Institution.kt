package com.example.budgetly.data.remote.remote_models.banking.institution

data class Institution(
    val id: String,
    val name: String,
    val bic: String? = null,
    val transaction_total_days: String = "90",
    val max_access_valid_for_days: String? = null,
    val countries: List<String>,
    val logo: String
)
