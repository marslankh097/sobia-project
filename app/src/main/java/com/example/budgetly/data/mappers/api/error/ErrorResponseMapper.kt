package com.example.budgetly.data.mappers.api.error
import com.example.budgetly.data.remote.remote_models.banking.error.ErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.domain.models.api.banking.error.ErrorResponseModel.toErrorResponse(): ErrorResponse {
    return ErrorResponse(
        summary = this.summary,
        detail = this.detail,
        type = this.type,
        status_code = this.status_code
    )
}

fun ErrorResponse.toErrorResponseModel(): com.example.budgetly.domain.models.api.banking.error.ErrorResponseModel {
    return com.example.budgetly.domain.models.api.banking.error.ErrorResponseModel(
        summary = this.summary,
        detail = this.detail,
        type = this.type,
        status_code = this.status_code
    )
}

fun List<com.example.budgetly.domain.models.api.banking.error.ErrorResponseModel>.toErrorResponseList(): List<ErrorResponse> =
    this.map { it.toErrorResponse() }

fun List<ErrorResponse>.toErrorResponseModelList(): List<com.example.budgetly.domain.models.api.banking.error.ErrorResponseModel> =
    this.map { it.toErrorResponseModel() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.error.ErrorResponseModel>>.toErrorResponseFlow(): Flow<List<ErrorResponse>> =
    this.map { it.toErrorResponseList() }

fun Flow<List<ErrorResponse>>.toErrorResponseModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.error.ErrorResponseModel>> =
    this.map { it.toErrorResponseModelList() }
