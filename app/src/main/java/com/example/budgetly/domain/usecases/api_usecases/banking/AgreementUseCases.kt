package com.example.budgetly.domain.usecases.api_usecases.banking

import com.example.budgetly.domain.repositories.api.banking.AgreementRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class AgreementUseCases @Inject constructor(
    val getEndUserAgreements: GetEndUserAgreements,
    val createEndUserAgreement: CreateEndUserAgreement,
    val getEndUserAgreementById: GetEndUserAgreementById,
    val deleteEndUserAgreement: DeleteEndUserAgreement,
    val acceptEndUserAgreement: AcceptEndUserAgreement
)
@Singleton
class GetEndUserAgreements @Inject constructor(
    private val repository: AgreementRepository
) {
    suspend operator fun invoke(): com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel {
        return repository.getEndUserAgreements()
    }
}
@Singleton
class CreateEndUserAgreement @Inject constructor(
    private val repository: AgreementRepository
) {
    suspend operator fun invoke(institutionId: String): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel {
        return repository.createEndUserAgreement(institutionId)
    }
}
@Singleton
class GetEndUserAgreementById @Inject constructor(
    private val repository: AgreementRepository
) {
    suspend operator fun invoke(id: String): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel {
        return repository.getEndUserAgreementById(id)
    }
}
@Singleton
class DeleteEndUserAgreement @Inject constructor(
    private val repository: AgreementRepository
) {
    suspend operator fun invoke(id: String): com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel {
        return repository.deleteEndUserAgreement(id)
    }
}
@Singleton
class AcceptEndUserAgreement @Inject constructor(
    private val repository: AgreementRepository
) {
    suspend operator fun invoke(id: String, request: com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel {
        return repository.acceptEndUserAgreement(id, request)
    }
}
