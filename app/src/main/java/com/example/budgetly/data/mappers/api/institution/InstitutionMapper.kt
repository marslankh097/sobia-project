package com.example.budgetly.data.mappers.api.institution

import com.example.budgetly.data.remote.remote_models.banking.institution.Institution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.data.remote.remote_models.banking.institution.Institution.toInstitutionModel(): com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel {
    return com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel(
        id = this.id,
        name = this.name,
        bic = this.bic,
        transaction_total_days = this.transaction_total_days,
        max_access_valid_for_days = this.max_access_valid_for_days,
        countries = this.countries,
        logo = this.logo
    )
}

fun com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel.toInstitution(): com.example.budgetly.data.remote.remote_models.banking.institution.Institution {
    return com.example.budgetly.data.remote.remote_models.banking.institution.Institution(
        id = this.id,
        name = this.name,
        bic = this.bic,
        transaction_total_days = this.transaction_total_days,
        max_access_valid_for_days = this.max_access_valid_for_days,
        countries = this.countries,
        logo = this.logo
    )
}

fun List<com.example.budgetly.data.remote.remote_models.banking.institution.Institution>.toInstitutionModelList(): List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel> =
    this.map { it.toInstitutionModel() }

fun List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel>.toInstitutionList(): List<com.example.budgetly.data.remote.remote_models.banking.institution.Institution> =
    this.map { it.toInstitution() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.institution.Institution>>.toInstitutionModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel>> =
    this.map { it.toInstitutionModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel>>.toInstitutionFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.institution.Institution>> =
    this.map { it.toInstitutionList() }

