package com.example.budgetly.data.remote.api.nordigen

import com.example.budgetly.data.remote.remote_models.banking.institution.Institution
import com.example.budgetly.data.remote.remote_models.banking.institution.InstitutionDetails
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface InstitutionApi {
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
}