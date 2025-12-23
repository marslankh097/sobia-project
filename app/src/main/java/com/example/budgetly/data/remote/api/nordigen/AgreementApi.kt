package com.example.budgetly.data.remote.api.nordigen
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAcceptanceDetailsRequest
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreementRequest
import com.example.budgetly.data.remote.remote_models.banking.agreements.PaginatedAgreementList
import com.example.budgetly.data.remote.remote_models.banking.error.SuccessfulDeleteResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AgreementApi {

    // region ─────── AGREEMENTS ───────
    @GET("agreements/enduser/")
    suspend fun getEndUserAgreements(
        @Header("Authorization") token: String
    ): PaginatedAgreementList

    @POST("agreements/enduser/")
    suspend fun createEndUserAgreement(
        @Header("Authorization") token: String,
        @Body request: EndUserAgreementRequest
    ): EndUserAgreement

    @GET("agreements/enduser/{id}/")
    suspend fun getEndUserAgreementById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): EndUserAgreement

    @DELETE("agreements/enduser/{id}/")
    suspend fun deleteEndUserAgreement(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): SuccessfulDeleteResponse

    @PUT("agreements/enduser/{id}/accept/")
    suspend fun acceptEndUserAgreement(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: EndUserAcceptanceDetailsRequest
    ): EndUserAgreement
}