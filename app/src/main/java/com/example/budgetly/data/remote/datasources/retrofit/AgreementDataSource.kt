package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAcceptanceDetailsRequest
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement
import com.example.budgetly.data.remote.remote_models.banking.agreements.PaginatedAgreementList
import com.example.budgetly.data.remote.remote_models.banking.error.SuccessfulDeleteResponse

interface AgreementDataSource {
    suspend fun getEndUserAgreements(): PaginatedAgreementList
    suspend fun createEndUserAgreement(institutionId: String): EndUserAgreement
    suspend fun getEndUserAgreementById(id: String): EndUserAgreement
    suspend fun deleteEndUserAgreement(id: String): SuccessfulDeleteResponse
    suspend fun acceptEndUserAgreement(id: String, request: EndUserAcceptanceDetailsRequest): EndUserAgreement
}