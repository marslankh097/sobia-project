package com.example.budgetly.data.remote.api.nordigen
import com.example.budgetly.data.remote.remote_models.banking.account.AccountBalance
import com.example.budgetly.data.remote.remote_models.banking.account.AccountDetail
import com.example.budgetly.data.remote.remote_models.banking.account.AccountTransactions
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAcceptanceDetailsRequest
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement
import com.example.budgetly.data.remote.remote_models.banking.account.Account
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreementRequest
import com.example.budgetly.data.remote.remote_models.banking.agreements.PaginatedAgreementList
import com.example.budgetly.data.remote.remote_models.banking.error.SuccessfulDeleteResponse
import com.example.budgetly.data.remote.remote_models.banking.requisition.PaginatedRequisitionList
import retrofit2.http.*

interface NordigenApiService {

    // region ─────── TOKEN ───────

    @POST("token/new/")
    suspend fun obtainToken(
        @Body request: com.example.budgetly.data.remote.remote_models.banking.token.TokenRequest
    ): com.example.budgetly.data.remote.remote_models.banking.token.TokenResponse

    @POST("token/refresh/")
    suspend fun refreshToken(
        @Body request: com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenRequest
    ): com.example.budgetly.data.remote.remote_models.banking.token.RefreshTokenResponse

    // endregion

    // region ─────── INSTITUTIONS ───────

//    @GET("institutions/")
//    suspend fun getInstitutions(): List<Institution>  
    @GET("institutions/")
    suspend fun getInstitutions(
    @Header("Authorization") token: String
    ): List<com.example.budgetly.data.remote.remote_models.banking.institution.Institution>

    @GET("institutions/{id}/")
    suspend fun getInstitutionById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails

    // endregion

    // region ─────── ACCOUNTS ───────

    @GET("accounts/{id}/")
    suspend fun getAccount(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Account

    @GET("accounts/{id}/balances/")
    suspend fun getAccountBalances(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AccountBalance

    @GET("accounts/{id}/details/")
    suspend fun getAccountDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AccountDetail

    @GET("accounts/{id}/transactions/")
    suspend fun getAccountTransactions(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AccountTransactions

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

    // region ─────── REQUISITIONS ───────

    @GET("requisitions/")
    suspend fun getRequisitions(
        @Header("Authorization") token: String
    ): PaginatedRequisitionList

    @POST("requisitions/")
    suspend fun createRequisition(
        @Header("Authorization") token: String,
        @Body request: com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest
    ): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition

    @GET("requisitions/{id}/")
    suspend fun getRequisitionById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition

    @DELETE("requisitions/{id}/")
    suspend fun deleteRequisitionById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): SuccessfulDeleteResponse

}

