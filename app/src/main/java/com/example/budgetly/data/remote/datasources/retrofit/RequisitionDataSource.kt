package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.remote_models.banking.account.Account
import com.example.budgetly.data.remote.remote_models.banking.error.SuccessfulDeleteResponse
import com.example.budgetly.data.remote.remote_models.banking.requisition.PaginatedRequisitionList

interface RequisitionDataSource {
    suspend fun getAccountsFromRequisition(requisitionId: String): List<Account>
    suspend fun getRequisitions(): PaginatedRequisitionList
    suspend fun createRequisition(institutionId: String, redirectUrl: String, agreementId: String? = null): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition
    suspend fun getRequisitionById(id: String): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition
    suspend fun deleteRequisitionById(id: String): SuccessfulDeleteResponse
}