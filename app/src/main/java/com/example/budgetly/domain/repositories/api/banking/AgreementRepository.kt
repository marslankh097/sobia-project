package com.example.budgetly.domain.repositories.api.banking

import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement

interface AgreementRepository {
    suspend fun getEndUserAgreements(): com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel
    suspend fun createEndUserAgreement(institutionId: String): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel
    suspend fun getEndUserAgreementById(id: String): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel
    suspend fun deleteEndUserAgreement(id: String): com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel
    suspend fun acceptEndUserAgreement(id: String, request: com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel
    suspend fun getCachedValidAgreement(institutionId: String): String?
    suspend fun storeAgreementLocally(agreement: EndUserAgreement)
}
