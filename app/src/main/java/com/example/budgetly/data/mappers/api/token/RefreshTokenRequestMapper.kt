package com.example.budgetly.data.mappers.api.token

import com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest
import com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel.toRefreshTokenRequest(): com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest {
    return com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest(
        refresh = this.refresh
    )
}

fun com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest.toRefreshTokenRequestModel(): com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel {
    return RefreshTokenRequestModel(
        refresh = this.refresh
    )
}

fun List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel>.toRefreshTokenRequestList(): List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest> =
    this.map { it.toRefreshTokenRequest() }

fun List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest>.toRefreshTokenRequestModelList(): List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel> =
    this.map { it.toRefreshTokenRequestModel() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel>>.toRefreshTokenRequestFlow( ): Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest>> =
    this.map { it.toRefreshTokenRequestList() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest>>.toRefreshTokenRequestModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel>> =
    this.map { it.toRefreshTokenRequestModelList() }
