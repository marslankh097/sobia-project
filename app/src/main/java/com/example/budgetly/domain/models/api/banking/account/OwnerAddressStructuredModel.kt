package com.example.budgetly.domain.models.api.banking.account


data class OwnerAddressStructuredModel(
    val streetName: String? = null,
    val buildingNumber: String? = null,
    val townName: String? = null,
    val postCode: String? = null,
    val country: String? = null
)