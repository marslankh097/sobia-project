package com.example.budgetly.data.remote.datasources.retrofit

import com.example.budgetly.data.remote.api.nordigen.AccountApi
import com.example.budgetly.data.remote.api.nordigen.RequisitionApi
import com.example.budgetly.data.remote.remote_models.banking.account.Account
import com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition
import com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest
import com.example.budgetly.utils.Utils.storeRequisition
import jakarta.inject.Inject
import java.util.UUID


class RequisitionDataSourceImpl @Inject constructor(
    private val api: RequisitionApi,
    private val accountApi: AccountApi,
    private val tokenProvider: TokenProvider,
    private val agreementDataSource: AgreementDataSource
): RequisitionDataSource {
    override suspend fun getAccountsFromRequisition(requisitionId: String): List<Account> {
        val token = tokenProvider.getToken()
        val accountIds = api.getRequisitionById(token, requisitionId).accounts
        return accountIds?.map { accountApi.getAccount(token, it) } ?: emptyList()
    }

    override suspend fun getRequisitions() = api.getRequisitions(tokenProvider.getToken())

    override suspend fun createRequisition(institutionId: String, redirectUrl: String, agreementId: String?): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition {
        val token = tokenProvider.getToken()
        val agreement = agreementId ?: agreementDataSource.createEndUserAgreement(institutionId).id
        val reference = UUID.randomUUID().toString()
        val request = createRequisitionRequest(institutionId, redirectUrl, agreement, reference)
        val requisition = api.createRequisition(token, request)
        storeRequisition(reference, requisition.id)
        return requisition
    }

    override suspend fun getRequisitionById(id: String) = api.getRequisitionById(tokenProvider.getToken(), id)

    override suspend fun deleteRequisitionById(id: String) = api.deleteRequisitionById(tokenProvider.getToken(), id)
}
private fun createRequisitionRequest(institutionId: String, redirectUrl: String,
                                     agreementId: String,
                                     reference: String = UUID.randomUUID().toString(),
                                     userLanguage: String = "EN"): com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest {
    return com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest(
        redirect = "$redirectUrl?ref=$reference",
        institution_id = institutionId,
        agreement = agreementId,
        reference = reference,
        user_language = userLanguage
    )
}