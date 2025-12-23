package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.api.nordigen.TokenApi
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class TokenProvider @Inject constructor(
    private val tokenApi: TokenApi,
    private val secretId: String,
    private val secretKey: String
) {
    private var accessToken: String? = null

    suspend fun getToken(): String {
        if (accessToken == null) {
            val response = tokenApi.obtainToken(
                com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest(
                    secretId,
                    secretKey
                )
            )
            accessToken = "Bearer ${response.access}"
        }
        return accessToken!!
    }
}


/*@Singleton
class TokenProvider @Inject constructor(
    private val retrofitFactory: RetrofitFactory
) {
    private val secretId = "your-secret-id"
    private val secretKey = "your-secret-key"

    private var accessToken: String? = null

    suspend fun getToken(): String {
        if (accessToken == null) {
            accessToken = "Bearer ${api.obtainToken(TokenRequest(secretId, secretKey)).access}"
        }
        return accessToken!!
    }
}*/
