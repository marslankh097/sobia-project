package com.example.budgetly.data.mappers.api.agreement

import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension function to map from EndUserAgreement to EndUserAgreementModel
fun EndUserAgreement.toEndUserAgreementModel(): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel {
    return com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel(
        id = this.id,
        created = this.created,
        institution_id = this.institution_id,
        max_historical_days = this.max_historical_days,
        access_valid_for_days = this.access_valid_for_days,
        access_scope = this.access_scope,
        accepted = this.accepted
    )
}

// Extension function to map from EndUserAgreementModel to EndUserAgreement
fun com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel.toEndUserAgreement(): EndUserAgreement {
    return EndUserAgreement(
        id = this.id,
        created = this.created,
        institution_id = this.institution_id,
        max_historical_days = this.max_historical_days,
        access_valid_for_days = this.access_valid_for_days,
        access_scope = this.access_scope,
        accepted = this.accepted
    )
}

// Extension function to map a list of EndUserAgreement to a list of EndUserAgreementModel
fun List<EndUserAgreement>.toEndUserAgreementModelList(): List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel> =
    this.map { it.toEndUserAgreementModel() }

// Extension function to map a list of EndUserAgreementModel to a list of EndUserAgreement
fun List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel>.toEndUserAgreementList(): List<EndUserAgreement> =
    this.map { it.toEndUserAgreement() }

// Extension function to map a flow of EndUserAgreement to a flow of EndUserAgreementModel
fun Flow<List<EndUserAgreement>>.toEndUserAgreementModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel>> =
    this.map { it.toEndUserAgreementModelList() }

// Extension function to map a flow of EndUserAgreementModel to a flow of EndUserAgreement
fun Flow<List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel>>.toEndUserAgreementFlow(): Flow<List<EndUserAgreement>> =
    this.map { it.toEndUserAgreementList() }
