package com.example.budgetly.data.mappers.api.agreement
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAcceptanceDetailsRequest
import com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun EndUserAcceptanceDetailsRequest.toEndUserAcceptanceDetailsRequestModel(): com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel {
    return EndUserAcceptanceDetailsRequestModel(
        user_agent = this.user_agent,
        ip_address = this.ip_address
    )
}

fun com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel.toEndUserAcceptanceDetailsRequest(): EndUserAcceptanceDetailsRequest {
    return EndUserAcceptanceDetailsRequest(
        user_agent = this.user_agent,
        ip_address = this.ip_address
    )
}

fun List<EndUserAcceptanceDetailsRequest>.toEndUserAcceptanceDetailsRequestModelList(): List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel> =
    this.map { it.toEndUserAcceptanceDetailsRequestModel() }

fun List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel>.toEndUserAcceptanceDetailsRequestList(): List<EndUserAcceptanceDetailsRequest> =
    this.map { it.toEndUserAcceptanceDetailsRequest() }

fun Flow<List<EndUserAcceptanceDetailsRequest>>.toEndUserAcceptanceDetailsRequestModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel>> =
    this.map { it.toEndUserAcceptanceDetailsRequestModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel>>.toEndUserAcceptanceDetailsRequestFlow(): Flow<List<EndUserAcceptanceDetailsRequest>> =
    this.map { it.toEndUserAcceptanceDetailsRequestList() }