package com.example.budgetly.data.repositories_impl.api_repo_impl.receipt

import com.example.budgetly.data.local.database.dao.api.receipt.ReceiptDao
import com.example.budgetly.data.mappers.api.receipt.toReceiptItemEntity
import com.example.budgetly.data.mappers.api.receipt.toReceiptItemEntityList
import com.example.budgetly.data.mappers.api.receipt.toReceiptItemModelFlow
import com.example.budgetly.data.mappers.api.receipt.toReceiptResponseEntity
import com.example.budgetly.data.mappers.api.receipt.toReceiptResponseModel
import com.example.budgetly.data.mappers.api.receipt.toReceiptResponseModelFlow
import com.example.budgetly.data.remote.datasources.retrofit.receipt.ReceiptRemoteDataSource
import com.example.budgetly.domain.models.api.receipt.ReceiptItemModel
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import com.example.budgetly.domain.repositories.api.receipt.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReceiptRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReceiptRemoteDataSource,
    private val receiptDao: ReceiptDao
) : ReceiptRepository {
    // ------------------------------
    // Upload & Persist to DB
    // ------------------------------
    override suspend fun uploadReceipt(file: MultipartBody.Part): List<ReceiptResponseModel> {
        val remoteList = remoteDataSource.uploadReceipt(file)
        val result = mutableListOf<ReceiptResponseModel>()
        remoteList.forEach { remote ->
            val responseEntity = remote.toReceiptResponseEntity()
            val receiptId = receiptDao.insertReceiptResponse(responseEntity)
            val itemEntities = remote.items.toReceiptItemEntityList(receiptId)
            receiptDao.insertReceiptItems(itemEntities)
            val responseModel = receiptDao.getReceiptById(receiptId)?.toReceiptResponseModel()
            responseModel?.let {
                result.add(it)
            }
        }
        return result
    }
    // ------------------------------
    // Insert
    // ------------------------------
    override suspend fun insertReceiptResponse(response: ReceiptResponseModel): Long {
        return receiptDao.insertReceiptResponse(response.toReceiptResponseEntity())
    }

    override suspend fun insertReceiptItems(receiptId: Long, items: List<ReceiptItemModel>) {
        receiptDao.insertReceiptItems(items.toReceiptItemEntityList(receiptId))
    }

    // ------------------------------
    // Get (Flow)
    // ------------------------------
    override fun getAllReceiptResponses(): Flow<List<ReceiptResponseModel>> {
        return receiptDao.getAllReceiptResponses().toReceiptResponseModelFlow()
    }

    override fun getItemsForReceipt(receiptId: Long): Flow<List<ReceiptItemModel>> {
        return receiptDao.getItemsForReceipt(receiptId).toReceiptItemModelFlow()
    }

    // ------------------------------
    // Get by ID
    // ------------------------------
    override suspend fun getReceiptById(receiptId: Long): ReceiptResponseModel? {
        return receiptDao.getReceiptById(receiptId)?.toReceiptResponseModel()
    }

    // ------------------------------
    // Delete
    // ------------------------------
    override suspend fun deleteReceiptResponseById(receiptId: Long) {
        receiptDao.deleteReceiptResponseById(receiptId)
    }

    override suspend fun deleteReceiptItemById(itemId: Long) {
        receiptDao.deleteReceiptItemById(itemId)
    }

    override suspend fun deleteAllReceipts() {
        receiptDao.deleteAllReceipts()
    }

    // ------------------------------
    // Update
    // ------------------------------
    override suspend fun updateReceiptItem(item: ReceiptItemModel) {
        val entity = item.toReceiptItemEntity(item.receiptId)
        receiptDao.updateReceiptItem(entity)
    }

    override suspend fun updateReceiptResponse(response: ReceiptResponseModel) {
        receiptDao.updateReceiptResponse(response.toReceiptResponseEntity())
    }
}


/*
@Singleton
class ReceiptRepositoryImpl @Inject constructor(
    private val remoteDataSource: ReceiptRemoteDataSource
) : ReceiptRepository {
    override suspend fun uploadReceipt(file: MultipartBody.Part): List<ReceiptResponseModel> {
        return remoteDataSource.uploadReceipt(file).toReceiptResponseModelList()
    }

    override suspend fun getReceipts(): List<ReceiptResponseModel> {
        return remoteDataSource.getReceipts().toReceiptResponseModelList()
    }
}*/
