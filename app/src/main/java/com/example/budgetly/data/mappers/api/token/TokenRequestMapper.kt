package com.example.budgetly.data.mappers.api.token

import com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest
import com.example.budgetly.domain.models.api.banking.token.TokenRequestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.domain.models.api.banking.token.TokenRequestModel.toTokenRequest(): com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest {
    return com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest(
        secret_id = this.secret_id,
        secret_key = this.secret_key
    )
}

fun com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest.toTokenRequestModel(): com.example.budgetly.domain.models.api.banking.token.TokenRequestModel {
    return TokenRequestModel(
        secret_id = this.secret_id,
        secret_key = this.secret_key
    )
}

fun List<com.example.budgetly.domain.models.api.banking.token.TokenRequestModel>.toTokenRequestList(): List<com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest> =
    this.map { it.toTokenRequest() }

fun List<com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest>.toTokenRequestModelList(): List<com.example.budgetly.domain.models.api.banking.token.TokenRequestModel> =
    this.map { it.toTokenRequestModel() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.token.TokenRequestModel>>.toTokenRequestFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest>> =
    this.map { it.toTokenRequestList() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest>>.toTokenRequestModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.token.TokenRequestModel>> =
    this.map { it.toTokenRequestModelList() }

