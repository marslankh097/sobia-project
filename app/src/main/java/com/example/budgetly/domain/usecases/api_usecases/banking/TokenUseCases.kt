package com.example.budgetly.domain.usecases.api_usecases.banking

import com.example.budgetly.domain.repositories.api.banking.TokenRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class TokenUseCases @Inject constructor(
    val obtainToken: ObtainToken,
    val refreshToken: RefreshToken
)
@Singleton
class ObtainToken @Inject constructor(
    private val repository: TokenRepository
) {
    suspend operator fun invoke(request: com.example.budgetly.domain.models.api.banking.token.TokenRequestModel): com.example.budgetly.domain.models.api.banking.token.TokenResponseModel {
        return repository.obtainToken(request)
    }
}
@Singleton
class RefreshToken @Inject constructor(
    private val repository: TokenRepository
) {
    suspend operator fun invoke(request: com.example.budgetly.domain.models.api.banking.token.RefreshTokenRequestModel): com.example.budgetly.domain.models.api.banking.token.RefreshTokenResponseModel {
        return repository.refreshToken(request)
    }
}
