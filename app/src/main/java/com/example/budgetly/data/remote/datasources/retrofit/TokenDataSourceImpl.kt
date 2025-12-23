package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.api.nordigen.TokenApi
import com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest
import com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest
import jakarta.inject.Inject

class TokenDataSourceImpl @Inject constructor(
    private val api: TokenApi
): TokenDataSource {
    override suspend fun obtainToken(request: com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest) = api.obtainToken(request)
    override suspend fun refreshToken(request: com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest) = api.refreshToken(request)
}
