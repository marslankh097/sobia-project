package com.example.budgetly.data.remote.api.nordigen

import com.example.budgetly.data.remote.remote_models.banking.error.SuccessfulDeleteResponse
import com.example.budgetly.data.remote.remote_models.banking.requisition.PaginatedRequisitionList
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RequisitionApi {
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