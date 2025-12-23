package com.example.budgetly.data.mappers.api.token

import com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse
import com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse.toRefreshTokenResponseModel(): com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel {
    return RefreshTokenResponseModel(
        access = this.access,
        access_expires = this.access_expires / 86400 // Convert seconds to days
    )
}

fun com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel.toRefreshTokenResponse(): com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse {
    return com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse(
        access = this.access,
        access_expires = this.access_expires * 86400 // Convert days to seconds
    )
}

fun List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse>.toRefreshTokenResponseModelList(): List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel> =
    this.map { it.toRefreshTokenResponseModel() }

fun List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel>.toRefreshTokenResponseList(): List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse> =
    this.map { it.toRefreshTokenResponse() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse>>.toRefreshTokenResponseModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel>> =
    this.map { it.toRefreshTokenResponseModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel>>.toRefreshTokenResponseFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse>> =
    this.map { it.toRefreshTokenResponseList() }

