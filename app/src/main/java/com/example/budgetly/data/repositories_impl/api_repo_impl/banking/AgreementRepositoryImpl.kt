package com.example.budgetly.data.repositories_impl.api_repo_impl.banking

import com.example.budgetly.data.local.database.dao.api.nordigen.AgreementDao
import com.example.budgetly.data.mappers.api.agreement.toCachedAgreement
import com.example.budgetly.data.mappers.api.agreement.toEndUserAcceptanceDetailsRequest
import com.example.budgetly.data.mappers.api.agreement.toEndUserAgreementModel
import com.example.budgetly.data.mappers.api.agreement.toPaginatedAgreementListModel
import com.example.budgetly.data.mappers.api.error.toSuccessfulDeleteResponseModel
import com.example.budgetly.data.remote.datasources.retrofit.AgreementDataSource
import com.example.budgetly.data.remote.remote_models.banking.agreements.EndUserAgreement
import com.example.budgetly.domain.repositories.api.banking.AgreementRepository
import com.example.budgetly.utils.log
import com.example.budgetly.utils.toComplexDateLong
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgreementRepositoryImpl @Inject constructor(
    private val agreementDataSource: AgreementDataSource,
    private val agreementDao: AgreementDao
) : AgreementRepository {

    override suspend fun getEndUserAgreements(): com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel = agreementDataSource.getEndUserAgreements().toPaginatedAgreementListModel()
    override suspend fun getEndUserAgreementById(id: String): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel = agreementDataSource.getEndUserAgreementById(id).toEndUserAgreementModel()
    override suspend fun deleteEndUserAgreement(id: String): com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel = agreementDataSource.deleteEndUserAgreement(id).toSuccessfulDeleteResponseModel()
    override suspend fun acceptEndUserAgreement(id: String, request: com.example.budgetly.domain.models.api.banking.agreement.EndUserAcceptanceDetailsRequestModel): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel =
        agreementDataSource.acceptEndUserAgreement(id, request.toEndUserAcceptanceDetailsRequest()).toEndUserAgreementModel()

    override suspend fun getCachedValidAgreement(institutionId: String): String? {
         val cached = agreementDao.getAgreementForInstitution(institutionId)
        log("getCachedValidAgreement: cached:$cached")
          if (cached != null) {
            try{
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = cached.created.toComplexDateLong()
                calendar.add(Calendar.DAY_OF_YEAR, cached.accessValidForDays)
                val validUntil = calendar.timeInMillis
                val now = System.currentTimeMillis()
            if (validUntil > now) {
                return cached.agreementId
            } else {
                agreementDao.deleteAgreement(institutionId) // expired
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: delete bad/invalid entry
            agreementDao.deleteAgreement(institutionId)
         }
        }
        return null
    }

    override suspend fun storeAgreementLocally(agreement: EndUserAgreement) {
        log("storeAgreementLocally: insertAgreement")
        agreementDao.insertAgreement(agreement.toCachedAgreement())
    }
    override suspend fun createEndUserAgreement(institutionId: String): com.example.budgetly.domain.models.api.banking.agreement.EndUserAgreementModel {
        log("createEndUserAgreement: before getCachedValidAgreement")
        getCachedValidAgreement(institutionId)?.let { cachedAgreementId ->
            val agreement =  agreementDataSource.getEndUserAgreementById(cachedAgreementId)
            log("createEndUserAgreement: cachedAgreementId:$cachedAgreementId")
            return   agreement.toEndUserAgreementModel()
        }
        val newAgreement = agreementDataSource.createEndUserAgreement(institutionId)
        storeAgreementLocally(newAgreement)
        log("createEndUserAgreement: newAgreement:$newAgreement")
        return newAgreement.toEndUserAgreementModel()
    }
}
