package com.example.budgetly.data.remote.api.nordigen

import com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest
import com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse
import com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest
import com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApi {
    // region ─────── TOKEN ───────

    @POST("token/new/")
    suspend fun obtainToken(
        @Body request: TokenRequest
    ): TokenResponse

    @POST("token/refresh/")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): RefreshTokenResponse

    // endregion
}