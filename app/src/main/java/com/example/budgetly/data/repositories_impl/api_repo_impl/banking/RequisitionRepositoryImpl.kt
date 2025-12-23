package com.example.budgetly.data.repositories_impl.api_repo_impl.banking

import com.example.budgetly.data.local.database.dao.api.nordigen.AgreementDao
import com.example.budgetly.data.local.database.dao.api.nordigen.RequisitionDao
import com.example.budgetly.data.mappers.api.account.toAccountModelList
import com.example.budgetly.data.mappers.api.error.toSuccessfulDeleteResponseModel
import com.example.budgetly.data.mappers.api.requisition.toCachedRequisition
import com.example.budgetly.data.mappers.api.requisition.toPaginatedRequisitionListModel
import com.example.budgetly.data.mappers.api.requisition.toRequisitionModel
import com.example.budgetly.data.remote.datasources.retrofit.RequisitionDataSource
import com.example.budgetly.data.remote.remote_models.banking.error.RequisitionStatus
import com.example.budgetly.domain.repositories.api.banking.RequisitionRepository
import com.example.budgetly.utils.Utils.storeRequisition
import com.example.budgetly.utils.log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequisitionRepositoryImpl @Inject constructor(
    private val requisitionDataSource: RequisitionDataSource,
    private val requisitionDao: RequisitionDao,
    private val agreementDao: AgreementDao
) : RequisitionRepository {

    override suspend fun getRequisitions(): com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel = requisitionDataSource.getRequisitions().toPaginatedRequisitionListModel()
    override suspend fun getRequisitionById(id: String): com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel = requisitionDataSource.getRequisitionById(id).toRequisitionModel()
    override suspend fun deleteRequisitionById(id: String): com.example.budgetly.domain.models.api.banking.error.SuccessfulDeleteResponseModel = requisitionDataSource.deleteRequisitionById(id).toSuccessfulDeleteResponseModel()
    override suspend fun getAccountsFromRequisition(requisitionId: String): List<com.example.budgetly.domain.models.api.banking.account.AccountModel> {
        return requisitionDataSource.getAccountsFromRequisition(requisitionId).toAccountModelList()
    }
    override suspend fun getCachedValidRequisition(institutionId: String,agreementId:String): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition? {
        val cached = requisitionDao.getRequisition(institutionId,agreementId)
        log("getCachedValidRequisition: before cached ! =null:$cached")
        if (cached != null) {
            val requisition = requisitionDataSource.getRequisitionById(cached.requisitionId)
            if (requisition.status == RequisitionStatus.LN.name || requisition.status == RequisitionStatus.GA.name) { // 'linked' or 'giving access'
                log("getCachedValidRequisition: before return cached")
                return requisition
            } else {
                log("getCachedValidRequisition:deleteRequisition")
                requisitionDao.deleteRequisition(institutionId) // Invalidate if status is not valid
                agreementDao.deleteAgreement(institutionId,) // Invalidate if status is not valid
            }
        }
        return null
    }
    override suspend fun storeRequisitionLocally(requisition: com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition) {
        requisitionDao.insertRequisition(requisition.toCachedRequisition())
    }
    override suspend fun createRequisition(
        institutionId: String,
        redirectUrl: String,
        agreementId: String?
    ): Pair<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel,Boolean> {
        log("createRequisition: before agreementId ! =null")
        if (agreementId != null) {
            getCachedValidRequisition(institutionId,agreementId)?.let { requisition ->
//                val requisition =  remoteDataSource.getRequisitionById(cached.requisitionId) // Reuse
                requisition.reference?.let { storeRequisition(it, requisition.id) }
                return   Pair(requisition.toRequisitionModel(),true)
//                cached.reference?.let { storeRequisition(it, cached.requisitionId) }
//                return Pair(cached.toRequisition().toRequisitionModel(),true)
            }
        }
        val requisition = requisitionDataSource.createRequisition(institutionId, redirectUrl, null)
        log("getCachedValidRequisition:inserttRequisition")
        requisitionDao.insertRequisition(requisition.toCachedRequisition())
        return Pair(requisition.toRequisitionModel(),false)
    }
}

