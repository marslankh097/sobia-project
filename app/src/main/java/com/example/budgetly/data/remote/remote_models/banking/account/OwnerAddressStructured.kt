package com.example.budgetly.data.remote.remote_models.banking.account


data class OwnerAddressStructured(
    val streetName: String? = null,
    val buildingNumber: String? = null,
    val townName: String? = null,
    val postCode: String? = null,
    val country: String? = null
)