package com.example.budgetly.domain.usecases.api_usecases.banking

import com.example.budgetly.domain.repositories.api.banking.InstitutionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class InstitutionUseCases @Inject constructor(
    val getInstitutions: GetInstitutions,
    val getInstitutionById: GetInstitutionById
)
@Singleton
class GetInstitutions @Inject constructor(
    private val repository: InstitutionRepository
) {
    suspend operator fun invoke(): List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel> {
        return repository.getInstitutions()
    }
}
@Singleton
class GetInstitutionById @Inject constructor(
    private val repository: InstitutionRepository
) {
    suspend operator fun invoke(id: String): com.example.budgetly.domain.models.api.banking.institiution.InstitutionDetailsModel {
        return repository.getInstitutionById(id)
    }
}
