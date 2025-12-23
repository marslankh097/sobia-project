package com.example.budgetly.data.mappers.api.agreement
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreementRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension function to map from EndUserAgreementRequest to EndUserAgreementRequestModel
fun EndUserAgreementRequest.toEndUserAgreementRequestModel(): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementRequestModel {
    return com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementRequestModel(
        institution_id = this.institution_id,
        max_historical_days = this.max_historical_days ?: 90,  // Default to 90 if null
        access_valid_for_days = this.access_valid_for_days ?: 90, // Default to 90 if null
        access_scope = this.access_scope ?: listOf(
            "balances",
            "details",
            "transactions"
        ) // Default to ["balances", "details", "transactions"] if null
    )
}

// Extension function to map from EndUserAgreementRequestModel to EndUserAgreementRequest
fun com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementRequestModel.toEndUserAgreementRequest(): EndUserAgreementRequest {
    return EndUserAgreementRequest(
        institution_id = this.institution_id,
        max_historical_days = this.max_historical_days ?: 90,  // Default to 90 if null
        access_valid_for_days = this.access_valid_for_days ?: 90, // Default to 90 if null
        access_scope = this.access_scope ?: listOf("balances", "details", "transactions") // Default to ["balances", "details", "transactions"] if null
    )
}

// Extension function to map a list of EndUserAgreementRequest to a list of EndUserAgreementRequestModel
fun List<EndUserAgreementRequest>.toEndUserAgreementRequestModelList(): List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementRequestModel> =
    this.map { it.toEndUserAgreementRequestModel() }

// Extension function to map a list of EndUserAgreementRequestModel to a list of EndUserAgreementRequest
fun List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementRequestModel>.toEndUserAgreementRequestList(): List<EndUserAgreementRequest> =
    this.map { it.toEndUserAgreementRequest() }

// Extension function to map a flow of EndUserAgreementRequest to a flow of EndUserAgreementRequestModel
fun Flow<List<EndUserAgreementRequest>>.toEndUserAgreementRequestModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementRequestModel>> =
    this.map { it.toEndUserAgreementRequestModelList() }

// Extension function to map a flow of EndUserAgreementRequestModel to a flow of EndUserAgreementRequest
fun Flow<List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementRequestModel>>.toEndUserAgreementRequestFlow(): Flow<List<EndUserAgreementRequest>> =
    this.map { it.toEndUserAgreementRequestList() }
