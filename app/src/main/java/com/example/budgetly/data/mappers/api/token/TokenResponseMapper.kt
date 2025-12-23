package com.example.budgetly.data.mappers.api.token

import com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse
import com.example.budgetly.domain.models.api.banking.token.TokenResponseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse.toTokenResponseModel(): com.example.budgetly.domain.models.api.banking.token.TokenResponseModel {
    return TokenResponseModel(
        access = this.access,
        access_expires = this.access_expires / 86400, // Convert seconds to days
        refresh = this.refresh,
        refresh_expires = this.refresh_expires / 86400 // Convert seconds to days
    )
}

fun com.example.budgetly.domain.models.api.banking.token.TokenResponseModel.toTokenResponse(): com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse {
    return com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse(
        access = this.access,
        access_expires = this.access_expires * 86400, // Convert days to seconds
        refresh = this.refresh,
        refresh_expires = this.refresh_expires * 86400 // Convert days to seconds
    )
}

fun List<com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse>.toTokenResponseModelList(): List<com.example.budgetly.domain.models.api.banking.token.TokenResponseModel> =
    this.map { it.toTokenResponseModel() }

fun List<com.example.budgetly.domain.models.api.banking.token.TokenResponseModel>.toTokenResponseList(): List<com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse> =
    this.map { it.toTokenResponse() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse>>.toTokenResponseModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.token.TokenResponseModel>> =
    this.map { it.toTokenResponseModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.token.TokenResponseModel>>.toTokenResponseFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse>> =
    this.map { it.toTokenResponseList() }
