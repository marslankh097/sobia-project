package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest
import com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse
import com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest
import com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse

interface TokenDataSource {
    suspend fun obtainToken(request: com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest): com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse
    suspend fun refreshToken(request: com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest): com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse
}