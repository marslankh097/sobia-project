package com.example.budgetly.data.repositories_impl.api_repo_impl.banking

import com.example.budgetly.data.mappers.api.token.toRefreshTokenRequest
import com.example.budgetly.data.mappers.api.token.toRefreshTokenResponseModel
import com.example.budgetly.data.mappers.api.token.toTokenRequest
import com.example.budgetly.data.mappers.api.token.toTokenResponseModel
import com.example.budgetly.data.remote.datasources.retrofit.TokenDataSource
import com.example.budgetly.domain.repositories.api.banking.TokenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {

    override suspend fun obtainToken(request: com.example.budgetly.domain.models.api.banking.token.TokenRequestModel): com.example.budgetly.domain.models.api.banking.token.TokenResponseModel {
       return tokenDataSource.obtainToken(request.toTokenRequest()).toTokenResponseModel()
    }
    override suspend fun refreshToken(request: com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel): com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel {
        return tokenDataSource.refreshToken(request.toRefreshTokenRequest()).toRefreshTokenResponseModel()
    }
}
