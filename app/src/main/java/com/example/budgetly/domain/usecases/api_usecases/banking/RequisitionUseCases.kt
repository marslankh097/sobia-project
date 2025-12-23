package com.example.budgetly.domain.usecases.api_usecases.banking

import com.example.budgetly.domain.repositories.api.banking.RequisitionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class RequisitionUseCases @Inject constructor(
    val getRequisitions: GetRequisitions,
    val createRequisition: CreateRequisition,
    val getRequisitionById: GetRequisitionById,
    val deleteRequisitionById: DeleteRequisitionById,
    val getAccountsFromRequisition: GetAccountsFromRequisition
)
@Singleton
class GetRequisitions @Inject constructor(
    private val repository: RequisitionRepository
) {
    suspend operator fun invoke(): com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel {
        return repository.getRequisitions()
    }
}
@Singleton
class CreateRequisition @Inject constructor(
    private val repository: RequisitionRepository
) {
    suspend operator fun invoke(
        institutionId: String,
        redirectUrl: String,
        agreementId: String? = null
    ): Pair<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel,Boolean> {
        return repository.createRequisition(institutionId, redirectUrl, agreementId)
    }
}
@Singleton
class GetRequisitionById @Inject constructor(
    private val repository: RequisitionRepository
) {
    suspend operator fun invoke(id: String): com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel {
        return repository.getRequisitionById(id)
    }
}
@Singleton
class DeleteRequisitionById @Inject constructor(
    private val repository: RequisitionRepository
) {
    suspend operator fun invoke(id: String): com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel {
        return repository.deleteRequisitionById(id)
    }
}
@Singleton
class GetAccountsFromRequisition @Inject constructor(
    private val repository: RequisitionRepository
) {
    suspend operator fun invoke(requisitionId: String): List<com.example.budgetly.domain.models.api.banking.account.AccountModel> {
        return repository.getAccountsFromRequisition(requisitionId)
    }
}

