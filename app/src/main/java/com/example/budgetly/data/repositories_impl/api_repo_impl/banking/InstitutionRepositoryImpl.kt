package com.example.budgetly.data.repositories_impl.api_repo_impl.banking

import com.example.budgetly.data.mappers.api.institution.toInstitutionDetailsModel
import com.example.budgetly.data.mappers.api.institution.toInstitutionModelList
import com.example.budgetly.data.remote.datasources.retrofit.InstitutionDataSource
import com.example.budgetly.domain.repositories.api.banking.InstitutionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstitutionRepositoryImpl @Inject constructor(
    private val institutionDataSource: InstitutionDataSource
) : InstitutionRepository {

    override suspend fun getInstitutions(): List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel> = institutionDataSource.getInstitutions().toInstitutionModelList()
    override suspend fun getInstitutionById(id: String): com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel = institutionDataSource.getInstitutionById(id).toInstitutionDetailsModel()
}
