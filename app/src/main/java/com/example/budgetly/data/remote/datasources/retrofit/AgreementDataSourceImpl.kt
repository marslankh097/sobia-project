package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.api.nordigen.AgreementApi
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAcceptanceDetailsRequest
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreementRequest
import jakarta.inject.Inject

class AgreementDataSourceImpl @Inject constructor(
    private val api: AgreementApi,
    private val tokenProvider: TokenProvider
): AgreementDataSource {
    override suspend fun getEndUserAgreements() = api.getEndUserAgreements(tokenProvider.getToken())

    override suspend fun createEndUserAgreement(institutionId: String): EndUserAgreement {
        val request = createEndUserAgreementRequest(institutionId)
        return api.createEndUserAgreement(tokenProvider.getToken(), request)
    }

    override suspend fun getEndUserAgreementById(id: String) = api.getEndUserAgreementById(tokenProvider.getToken(), id)

    override suspend fun deleteEndUserAgreement(id: String) = api.deleteEndUserAgreement(tokenProvider.getToken(), id)

    override suspend fun acceptEndUserAgreement(id: String, request: EndUserAcceptanceDetailsRequest)
        = api.acceptEndUserAgreement(tokenProvider.getToken(), id, request)
}
private fun createEndUserAgreementRequest(
    institutionId: String,
    maxHistoricalDays: Int = 90,  // Default 90 days if not specified
    accessValidForDays: Int = 30,  // Default 30 days if not specified
    accessScope: List<String> = listOf("balances", "details", "transactions")  // Default scope
): EndUserAgreementRequest {
    return EndUserAgreementRequest(
        institution_id = institutionId,
        max_historical_days = maxHistoricalDays,
        access_valid_for_days = accessValidForDays,
        access_scope = accessScope
    )
}
