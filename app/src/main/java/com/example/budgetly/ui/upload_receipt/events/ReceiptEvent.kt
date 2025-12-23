package com.example.budgetly.ui.upload_receipt.events

import android.content.Context
import android.net.Uri
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import java.io.File

sealed class ReceiptEvent {
    data object ObserveAllReceipts : ReceiptEvent()
    data class UploadUri(val uri: Uri) : ReceiptEvent()
    data class UploadFile(val file: File) : ReceiptEvent()
    data class UploadFileFromUri(val context: Context, val isCamera: Boolean = false) : ReceiptEvent()
    data object Process : ReceiptEvent()
    data class Retry(val context:Context) : ReceiptEvent()
    data object ClearState : ReceiptEvent()
    data object ResetState : ReceiptEvent()
    data class DisplaySettingPermissionDialog(val display: Boolean) : ReceiptEvent()
    data class SetSettingDialogType(val isCamera: Boolean) : ReceiptEvent()
    data class SetTempReceiptId(val receiptId: Long) : ReceiptEvent()
    data class SetTempItemId(val itemId: Long) : ReceiptEvent()
    data class SelectReceipt(val receipt:ReceiptResponseModel) : ReceiptEvent()
    object MarkNavigationHandled : ReceiptEvent()


    data class ToggleReceipt(val receiptId: Long) : ReceiptEvent()
    data class ToggleReceiptItem(val itemId: Long) : ReceiptEvent()

    data object DeleteItem : ReceiptEvent()
    data class ShowDeleteAllDialog(val show:Boolean) : ReceiptEvent()
    data class ShowDeleteReceiptDialog(val show:Boolean) : ReceiptEvent()
    data class ShowDeleteItemDialog(val show:Boolean) : ReceiptEvent()
    data object DeleteReceipt : ReceiptEvent()
    data object DeleteAllReceipts : ReceiptEvent()
}
