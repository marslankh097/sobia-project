package com.example.budgetly.data.mappers.api.institution

import com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails.toInstitutionDetailsModel(): com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel {
    return com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel(
        id = this.id,
        name = this.name,
        bic = this.bic,
        transaction_total_days = this.transaction_total_days,
        max_access_valid_for_days = this.max_access_valid_for_days,
        countries = this.countries,
        logo = this.logo,
        supported_features = this.supported_features,
        identification_codes = this.identification_codes
    )
}

fun com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel.toInstitutionDetails(): com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails {
    return com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails(
        id = this.id,
        name = this.name,
        bic = this.bic,
        transaction_total_days = this.transaction_total_days,
        max_access_valid_for_days = this.max_access_valid_for_days,
        countries = this.countries,
        logo = this.logo,
        supported_features = this.supported_features,
        identification_codes = this.identification_codes
    )
}

fun List<com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails>.toInstitutionDetailsModelList(): List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel> =
    this.map { it.toInstitutionDetailsModel() }

fun List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel>.toInstitutionDetailsList(): List<com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails> =
    this.map { it.toInstitutionDetails() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails>>.toInstitutionDetailsModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel>> =
    this.map { it.toInstitutionDetailsModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel>>.toInstitutionDetailsFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails>> =
    this.map { it.toInstitutionDetailsList() }

