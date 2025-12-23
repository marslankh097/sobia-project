package com.example.budgetly.data.mappers.api.account

import com.example.budgetly.data.remote.remote_models.banking.account.OwnerAddressStructured
import com.example.budgetly.domain.models.api.banking.account.OwnerAddressStructuredModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun OwnerAddressStructured.toOwnerAddressStructuredModel(): com.example.budgetly.domain.models.api.banking.account.OwnerAddressStructuredModel {
    return OwnerAddressStructuredModel(
        streetName = this.streetName,
        buildingNumber = this.buildingNumber,
        townName = this.townName,
        postCode = this.postCode,
        country = this.country
    )
}

fun com.example.budgetly.domain.models.api.banking.account.OwnerAddressStructuredModel.toOwnerAddressStructured(): OwnerAddressStructured {
    return OwnerAddressStructured(
        streetName = this.streetName,
        buildingNumber = this.buildingNumber,
        townName = this.townName,
        postCode = this.postCode,
        country = this.country
    )
}

fun List<OwnerAddressStructured>.toOwnerAddressStructuredModelList(): List<com.example.budgetly.domain.models.api.banking.account.OwnerAddressStructuredModel> =
    this.map { it.toOwnerAddressStructuredModel() }

fun List<com.example.budgetly.domain.models.api.banking.account.OwnerAddressStructuredModel>.toOwnerAddressStructuredList(): List<OwnerAddressStructured> =
    this.map { it.toOwnerAddressStructured() }

fun Flow<List<OwnerAddressStructured>>.toOwnerAddressStructuredModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.account.OwnerAddressStructuredModel>> =
    this.map { it.toOwnerAddressStructuredModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.account.OwnerAddressStructuredModel>>.toOwnerAddressStructuredFlow(): Flow<List<OwnerAddressStructured>> =
    this.map { it.toOwnerAddressStructuredList() }
