package com.example.budgetly.data.mappers.api.agreement

import com.example.budgetly.data.local.database.entities.api.nordigen.CachedAgreement
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun EndUserAgreement.toCachedAgreement(): CachedAgreement {
    return CachedAgreement(
        agreementId = this.id,
        institutionId = this.institution_id,
        created = this.created,
        accessValidForDays = this.access_valid_for_days
    )
}


fun CachedAgreement.toEndUserAgreement(): EndUserAgreement {
    return EndUserAgreement(
        id = this.agreementId,
        institution_id = this.institutionId,
        created = this.created,
        access_valid_for_days = this.accessValidForDays,
        max_historical_days = 90,
        access_scope = listOf("balances", "details", "transactions"),
        accepted = true
    )
}

fun List<EndUserAgreement>.toCachedAgreementList(): List<CachedAgreement> =
    this.map { it.toCachedAgreement() }

fun List<CachedAgreement>.toEndUserAgreementList(): List<EndUserAgreement> =
    this.map { it.toEndUserAgreement() }

fun Flow<List<EndUserAgreement>>.toCachedAgreementFlow(): Flow<List<CachedAgreement>> =
    this.map { it.toCachedAgreementList() }

fun Flow<List<CachedAgreement>>.toEndUserAgreementFlow(): Flow<List<EndUserAgreement>> =
    this.map { it.toEndUserAgreementList() }
