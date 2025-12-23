package com.example.budgetly.domain.repositories.api.banking

import com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition

interface RequisitionRepository {
    suspend fun getRequisitions(): com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel
    suspend fun createRequisition(institutionId: String, redirectUrl: String, agreementId: String? = null): Pair<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel,Boolean>
    suspend fun getRequisitionById(id: String): com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel
    suspend fun deleteRequisitionById(id: String): com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel
    suspend fun getAccountsFromRequisition(requisitionId: String): List<com.example.budgetly.domain.models.api.banking.account.AccountModel>
    suspend fun getCachedValidRequisition(institutionId: String,agreementId:String): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition?
    suspend fun storeRequisitionLocally(requisition: com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition)
}
