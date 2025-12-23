package com.example.budgetly.domain.usecases.api_usecases.receipt

import com.example.budgetly.domain.models.api.receipt.ReceiptItemModel
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import com.example.budgetly.domain.repositories.api.receipt.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
data class ReceiptUseCases @Inject constructor(
    val uploadReceipt: UploadReceiptUseCase,
    val insertReceiptResponse: InsertReceiptResponseUseCase,
    val insertReceiptItems: InsertReceiptItemsUseCase,
    val getAllReceiptResponses: GetAllReceiptResponsesUseCase,
    val getItemsForReceipt: GetItemsForReceiptUseCase,
    val getReceiptById: GetReceiptByIdUseCase,
    val deleteReceiptResponseById: DeleteReceiptResponseByIdUseCase,
    val deleteReceiptItemById: DeleteReceiptItemByIdUseCase,
    val deleteAllReceipts: DeleteAllReceiptsUseCase,
    val updateReceiptItem: UpdateReceiptItemUseCase,
    val updateReceiptResponse: UpdateReceiptResponseUseCase
)
@Singleton
class UpdateReceiptResponseUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(response: ReceiptResponseModel) {
        repository.updateReceiptResponse(response)
    }
}
@Singleton
class UpdateReceiptItemUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(item: ReceiptItemModel) {
        repository.updateReceiptItem(item)
    }
}
@Singleton
class DeleteAllReceiptsUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllReceipts()
    }
}
@Singleton
class DeleteReceiptItemByIdUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(itemId: Long) {
        repository.deleteReceiptItemById(itemId)
    }
}
@Singleton
class DeleteReceiptResponseByIdUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receiptId: Long) {
        repository.deleteReceiptResponseById(receiptId)
    }
}
@Singleton
class GetReceiptByIdUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receiptId: Long): ReceiptResponseModel? {
        return repository.getReceiptById(receiptId)
    }
}
@Singleton
class GetItemsForReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    operator fun invoke(receiptId: Long): Flow<List<ReceiptItemModel>> {
        return repository.getItemsForReceipt(receiptId)
    }
}
@Singleton
class GetAllReceiptResponsesUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    operator fun invoke(): Flow<List<ReceiptResponseModel>> {
        return repository.getAllReceiptResponses()
    }
}
@Singleton
class InsertReceiptItemsUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receiptId: Long, items: List<ReceiptItemModel>) {
        repository.insertReceiptItems(receiptId, items)
    }
}
@Singleton
class InsertReceiptResponseUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(response: ReceiptResponseModel): Long {
        return repository.insertReceiptResponse(response)
    }
}
@Singleton
class UploadReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(file: MultipartBody.Part): List<ReceiptResponseModel> {
        return repository.uploadReceipt(file)
    }
}


/*@Singleton
data
 class ReceiptUseCase @Inject constructor(
    val uploadReceipt: UploadReceipt,
    val getReceipts: GetReceipts
)
@Singleton
class UploadReceipt @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(file: MultipartBody.Part): List< ReceiptResponseModel> {
        return repository.uploadReceipt(file)
    }
}

@Singleton
class GetReceipts @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(): List<ReceiptResponseModel> {
        return repository.getReceipts()
    }
}*/
