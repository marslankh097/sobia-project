package com.example.budgetly.domain.repositories.api.banking

import com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel
import com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel

interface InstitutionRepository {
    suspend fun getInstitutions(): List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel>
    suspend fun getInstitutionById(id: String): com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel
}
