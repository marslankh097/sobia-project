package com.example.budgetly.domain.models.api.banking.institiution

data class InstitutionDetailsModel(
    val id: String,
    val name: String,
    val bic: String? = null,
    val transaction_total_days: String = "90",
    val max_access_valid_for_days: String? = null,
    val countries: List<String>,
    val logo: String,
    val supported_features: List<Any> = emptyList(),
    val identification_codes: List<Any> = emptyList()
)
