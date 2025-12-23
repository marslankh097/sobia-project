package com.example.budgetly.data.mappers.api.error

import com.example.budgetly.data.remote.remote_models.banking.error.SuccessfulDeleteResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel.toSuccessfulDeleteResponse(): SuccessfulDeleteResponse {
    return SuccessfulDeleteResponse(
        summary = this.summary,
        detail = this.detail
    )
}

fun SuccessfulDeleteResponse.toSuccessfulDeleteResponseModel(): com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel {
    return com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel(
        summary = this.summary,
        detail = this.detail
    )
}

fun List<com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel>.toSuccessfulDeleteResponseList(): List<SuccessfulDeleteResponse> =
    this.map { it.toSuccessfulDeleteResponse() }

fun List<SuccessfulDeleteResponse>.toSuccessfulDeleteResponseModelList(): List<com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel> =
    this.map { it.toSuccessfulDeleteResponseModel() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel>>.toSuccessfulDeleteResponseFlow(): Flow<List<SuccessfulDeleteResponse>> =
    this.map { it.toSuccessfulDeleteResponseList() }

fun Flow<List<SuccessfulDeleteResponse>>.toSuccessfulDeleteResponseModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel>> =
    this.map { it.toSuccessfulDeleteResponseModelList() }
