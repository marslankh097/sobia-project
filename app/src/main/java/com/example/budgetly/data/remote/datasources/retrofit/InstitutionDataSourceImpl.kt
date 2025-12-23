package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.api.nordigen.InstitutionApi
import com.example.budgetly.data.remote.remote_models.banking.institution.Institution
import com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails
import jakarta.inject.Inject


class InstitutionDataSourceImpl @Inject constructor(
    private val api: InstitutionApi,
    private val tokenProvider: TokenProvider
): InstitutionDataSource {
    override suspend fun getInstitutions(): List<com.example.budgetly.data.remote.remote_models.banking.institution.Institution> {
        val token = tokenProvider.getToken()
        return api.getInstitutions(token).filter {
            it.name.contains("sandbox", true) || it.name.contains("test", true)
        }
    }

    override suspend fun getInstitutionById(id: String): com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails {
        val token = tokenProvider.getToken()
        return api.getInstitutionById(token, id)
    }
}
