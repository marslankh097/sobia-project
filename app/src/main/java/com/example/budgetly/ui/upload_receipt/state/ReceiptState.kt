package com.example.budgetly.ui.upload_receipt.state

import android.net.Uri
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import java.io.File

data class ReceiptState(
    val uploadedImageUri: Uri? = null,
    val uploadedFile: File? = null,
    val selectedReceiptId: Long? = null,
    val tempReceiptId: Long? = null,
    val tempItemId: Long? = null,
    val showDeleteAllDialog: Boolean = false,
    val showDeleteReceiptDialog: Boolean = false,
    val showDeleteItemDialog: Boolean = false,
    val shouldNavigateToResultOnce: Boolean = false,
    val uiState: ReceiptUiState = ReceiptUiState.Idle
)

fun ReceiptUiState.receiptsOrEmpty(): List<ReceiptResponseModel> {
    return if (this is ReceiptUiState.Success) this.receipts else emptyList()
}