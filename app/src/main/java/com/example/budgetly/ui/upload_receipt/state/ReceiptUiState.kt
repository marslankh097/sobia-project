package com.example.budgetly.ui.upload_receipt.state

import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel

// --- UI State ---
sealed interface ReceiptUiState {
    object Idle : ReceiptUiState
    object Loading : ReceiptUiState
    data class Success(
        val receipts: List<ReceiptResponseModel>,
        val selectedReceipt: ReceiptResponseModel? = null
    ) : ReceiptUiState
    data class Error(val message: String) : ReceiptUiState
}
//data class ReceiptUiState(
//    val isLoading: Boolean = false,
//    val isProcessing: Boolean = false,
//    val error: String? = null,
//    val  selectedReceiptId: Long? = null,
//    val uploadedImageUri: Uri? = null,
//    val uploadedFile: File? = null,
//    val receipt: ReceiptResponseModel? = null,
//    val tempReceiptId:Long? = null,
//    val tempItemId:Long? = null,
//    val showDeleteAllDialog:Boolean = false,
//    val showDeleteReceiptDialog:Boolean = false,
//    val showDeleteItemDialog:Boolean = false,
//    val receipts:List<ReceiptResponseModel> = emptyList()
//)
