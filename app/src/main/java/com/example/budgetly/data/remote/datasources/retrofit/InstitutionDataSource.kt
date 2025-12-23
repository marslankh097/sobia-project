package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.remote_models.banking.institution.Institution
import com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails

interface InstitutionDataSource {
    suspend fun getInstitutions(): List<com.example.budgetly.data.remote.remote_models.banking.institution.Institution>
    suspend fun getInstitutionById(id: String): com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails
}