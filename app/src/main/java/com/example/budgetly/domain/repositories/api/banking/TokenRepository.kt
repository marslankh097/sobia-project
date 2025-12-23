package com.example.budgetly.domain.repositories.api.banking

interface TokenRepository {
    suspend fun obtainToken(request: com.example.budgetly.domain.models.api.banking.token.TokenRequestModel): com.example.budgetly.domain.models.api.banking.token.TokenResponseModel
    suspend fun refreshToken(request: com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel): com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel
}
