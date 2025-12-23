package com.example.budgetly.ui.upload_receipt

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.domain.models.api.receipt.ReceiptResponseModel
import com.example.budgetly.domain.usecases.api_usecases.receipt.ReceiptUseCases
import com.example.budgetly.ui.upload_receipt.events.ReceiptEvent
import com.example.budgetly.ui.upload_receipt.state.ReceiptState
import com.example.budgetly.ui.upload_receipt.state.ReceiptUiState
import com.example.budgetly.ui.upload_receipt.state.receiptsOrEmpty
import com.example.budgetly.utils.toCompressedFile
import com.example.budgetly.utils.toFileSafely
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val receiptUseCase: ReceiptUseCases
) : ViewModel() {

    private val _receiptState = MutableStateFlow(ReceiptState())
     val receiptState: StateFlow<ReceiptState> = _receiptState.asStateFlow()

    private val _displaySettingDialog = MutableStateFlow(Pair(false, false))
    val displaySettingDialog: StateFlow<Pair<Boolean, Boolean>> = _displaySettingDialog.asStateFlow()

    private val _expandedReceipts = MutableStateFlow<List<Long>>(emptyList())
    val expandedReceipts: StateFlow<List<Long>> = _expandedReceipts.asStateFlow()

    private val _expandedReceiptItems = MutableStateFlow<List<Long>>(emptyList())
    val expandedReceiptItems: StateFlow<List<Long>> = _expandedReceiptItems.asStateFlow()

    private var receiptJob: Job? = null

    init {
        onEvent(ReceiptEvent.ObserveAllReceipts)
    }

    fun onEvent(event: ReceiptEvent) {
        when (event) {
            is ReceiptEvent.UploadUri -> _receiptState.update { it.copy(uploadedImageUri = event.uri) }
            is ReceiptEvent.UploadFile -> _receiptState.update { it.copy(uploadedFile = event.file) }

            ReceiptEvent.Process -> processReceipt()
            is ReceiptEvent.Retry -> retryProcessing(event.context)

            ReceiptEvent.ClearState -> _receiptState.update {
                it.copy(
                    selectedReceiptId = null,
                    uploadedFile = null,
                    uploadedImageUri = null,
                    uiState = ReceiptUiState.Idle
                )
            }

            is ReceiptEvent.DisplaySettingPermissionDialog -> _displaySettingDialog.update { it.copy(first = event.display) }
            is ReceiptEvent.SetSettingDialogType -> _displaySettingDialog.update { it.copy(second = event.isCamera) }

            is ReceiptEvent.UploadFileFromUri -> {
                receiptState.value.uploadedImageUri?.let {
                    if (event.isCamera) convertUriToCompressedFile(event.context, it)
                    else convertUriToFile(event.context, it)
                }
            }

            ReceiptEvent.ObserveAllReceipts -> observeAllReceipts()
            is ReceiptEvent.SetTempItemId -> setTempItemId(event.itemId)
            is ReceiptEvent.SetTempReceiptId -> setTempReceiptId(event.receiptId)
            ReceiptEvent.DeleteAllReceipts -> deleteAllReceipts()
            ReceiptEvent.DeleteItem -> receiptState.value.tempItemId?.let { handleDeleteReceiptItem(it) }
            ReceiptEvent.DeleteReceipt -> receiptState.value.tempReceiptId?.let { handleDeleteReceipt(it) }
            is ReceiptEvent.SelectReceipt -> handleSelectReceipt(event.receipt)
            is ReceiptEvent.ShowDeleteAllDialog -> showDeleteAllDialog(event.show)
            is ReceiptEvent.ShowDeleteItemDialog -> showDeleteItemDialog(event.show)
            is ReceiptEvent.ShowDeleteReceiptDialog -> showDeleteReceiptDialog(event.show)
            is ReceiptEvent.ToggleReceipt -> toggleReceipt(event.receiptId)
            is ReceiptEvent.ToggleReceiptItem -> toggleReceiptItem(event.itemId)
            ReceiptEvent.ResetState -> _receiptState.update { it.copy(uiState = ReceiptUiState.Idle) }
            is ReceiptEvent.MarkNavigationHandled -> markNavigationHandled()

        }
    }

    private fun observeAllReceipts() {
        receiptJob?.cancel()
        receiptJob = viewModelScope.launch {
            receiptUseCase.getAllReceiptResponses().collect { receipts ->
                val selected = receipts.firstOrNull { it.receiptId == _receiptState.value.selectedReceiptId }
                _receiptState.update {
                    it.copy(
                        uiState = ReceiptUiState.Success(
                            receipts = receipts,
                            selectedReceipt = selected
                        )
                    )
                }
            }
        }
    }

    private fun processReceipt() {
        val file = _receiptState.value.uploadedFile ?: return
        _receiptState.update { it.copy(uiState = ReceiptUiState.Loading) }

        viewModelScope.launch {
            try {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val result = receiptUseCase.uploadReceipt(multipart)

                val receipts = _receiptState.value.uiState.receiptsOrEmpty()
                val newReceipt = result.firstOrNull()
                _receiptState.update {
                    it.copy(
                        selectedReceiptId = newReceipt?.receiptId,
                        shouldNavigateToResultOnce = newReceipt != null,
                        uiState = if (newReceipt != null) {
                            ReceiptUiState.Success(receipts, newReceipt)
                        } else {
                            ReceiptUiState.Error("No receipt data returned")
                        }
                    )
                }
            } catch (e: Exception) {
                _receiptState.update {
                    it.copy(uiState = ReceiptUiState.Error(e.message ?: "Upload failed"))
                }
            }
        }
    }


    private fun retryProcessing(context: Context) {
        val currentFile = _receiptState.value.uploadedFile
        val currentUri = _receiptState.value.uploadedImageUri

        if (currentFile == null || !currentFile.exists()) {
            if (currentUri != null) {
                onEvent(ReceiptEvent.UploadUri(currentUri))
                onEvent(ReceiptEvent.UploadFileFromUri(context))
                onEvent(ReceiptEvent.Process)
            } else {
                _receiptState.update {
                    it.copy(uiState = ReceiptUiState.Error("No file or URI available to retry"))
                }
            }
        } else {
            onEvent(ReceiptEvent.Process)
        }
    }

    private fun convertUriToFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val file = uri.toFileSafely(context)
                _receiptState.update {
                    if (file != null) it.copy(uploadedFile = file)
                    else it.copy(uiState = ReceiptUiState.Error("Failed to convert image to file"))
                }
            } catch (e: Exception) {
                _receiptState.update { it.copy(uiState = ReceiptUiState.Error("Unable to read image")) }
            }
        }
    }

    private fun convertUriToCompressedFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val file = uri.toCompressedFile(context)
                _receiptState.update {
                    if (file != null) it.copy(uploadedFile = file)
                    else it.copy(uiState = ReceiptUiState.Error("Failed to convert image to file"))
                }
            } catch (e: Exception) {
                _receiptState.update { it.copy(uiState = ReceiptUiState.Error("Unable to read image")) }
            }
        }
    }

    private fun deleteAllReceipts() {
        _receiptState.update { it.copy(uiState = ReceiptUiState.Loading) }
        viewModelScope.launch {
            receiptUseCase.deleteAllReceipts()
            _receiptState.update {
                it.copy(
                    selectedReceiptId = null,
                    uiState = ReceiptUiState.Idle
                )
            }
        }
    }

    private fun handleDeleteReceipt(receiptId: Long) {
        viewModelScope.launch {
            receiptUseCase.deleteReceiptResponseById(receiptId)
            if (_receiptState.value.selectedReceiptId == receiptId) {
                _receiptState.update {
                    it.copy(
                        selectedReceiptId = null,
                        uiState = ReceiptUiState.Idle
                    )
                }
            }
        }
    }

    private fun handleDeleteReceiptItem(receiptItemId: Long) {
        viewModelScope.launch {
            receiptUseCase.deleteReceiptItemById(receiptItemId)
        }
    }

    private fun handleSelectReceipt(receipt: ReceiptResponseModel) {
        _receiptState.update {
            it.copy(
                selectedReceiptId = receipt.receiptId,
                uiState = ReceiptUiState.Success(
                    receipts = it.uiState.receiptsOrEmpty(),
                    selectedReceipt = receipt
                )
            )
        }
    }

    private fun showDeleteAllDialog(show: Boolean) {
        _receiptState.update { it.copy(showDeleteAllDialog = show) }
    }
    private fun markNavigationHandled() {
        _receiptState.update { it.copy(shouldNavigateToResultOnce = false) }
    }

    private fun showDeleteItemDialog(show: Boolean) {
        _receiptState.update { it.copy(showDeleteItemDialog = show) }
    }

    private fun showDeleteReceiptDialog(show: Boolean) {
        _receiptState.update { it.copy(showDeleteReceiptDialog = show) }
    }

    private fun toggleReceipt(receiptId: Long) {
        _expandedReceipts.value =
            if (receiptId in _expandedReceipts.value)
                _expandedReceipts.value - receiptId
            else
                _expandedReceipts.value + receiptId
    }

    private fun toggleReceiptItem(receiptItemId: Long) {
        _expandedReceiptItems.value =
            if (receiptItemId in _expandedReceiptItems.value)
                _expandedReceiptItems.value - receiptItemId
            else
                _expandedReceiptItems.value + receiptItemId
    }

    private fun setTempReceiptId(receiptId: Long) {
        _receiptState.update { it.copy(tempReceiptId = receiptId) }
    }

    private fun setTempItemId(itemId: Long) {
        _receiptState.update { it.copy(tempItemId = itemId) }
    }
}

/*@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val receiptUseCase: ReceiptUseCases
) : ViewModel() {

    private val _receiptState = MutableStateFlow(ReceiptUiState())
    val receiptState: StateFlow<ReceiptUiState> = _receiptState.asStateFlow()

    private val _displaySettingDialog = MutableStateFlow(Pair(false, false))
    val displaySettingDialog: StateFlow<Pair<Boolean, Boolean>> =
        _displaySettingDialog.asStateFlow()
    private val _expandedReceipts = MutableStateFlow<List<Long>>(mutableListOf())
    val expandedReceipts: StateFlow<List<Long>> = _expandedReceipts.asStateFlow()

    private fun toggleReceipt(receiptId:Long){
        if(_expandedReceipts.value.contains(receiptId)){
            _expandedReceipts.value =  _expandedReceipts.value.filter { it != receiptId }
        }else{
            _expandedReceipts.value += receiptId
        }
    }

    private val _expandedReceiptItems = MutableStateFlow<List<Long>>(mutableListOf())
    val expandedReceiptItems: StateFlow<List<Long>> = _expandedReceiptItems.asStateFlow()

    private fun toggleReceiptItem(receiptItemId:Long){
        if(_expandedReceiptItems.value.contains(receiptItemId)){
            _expandedReceiptItems.value =  _expandedReceiptItems.value.filter { it != receiptItemId }
        }else{
            _expandedReceiptItems.value += receiptItemId
        }
    }
    init {
        onEvent(ReceiptEvent.ObserveAllReceipts)
    }
    private var processJob: Job? = null
    private var receiptJob: Job? = null
    fun onEvent(event: ReceiptEvent) {
        when (event) {
            is ReceiptEvent.UploadUri -> {
                _receiptState.update { it.copy(uploadedImageUri = event.uri) }
            }

            is ReceiptEvent.UploadFile -> {
                _receiptState.update { it.copy(uploadedFile = event.file) }
            }

            ReceiptEvent.Process -> processReceipt()

            is ReceiptEvent.Retry -> retryProcessing(event.context)

            ReceiptEvent.ClearState -> _receiptState.update { it.copy(error = null, receipt = null, isProcessing = false,selectedReceiptId = null,) }

            is ReceiptEvent.DisplaySettingPermissionDialog -> {
                _displaySettingDialog.update { it.copy(first = event.display) }
            }

            is ReceiptEvent.SetSettingDialogType -> {
                _displaySettingDialog.update { it.copy(second = event.isCamera) }
            }

            is ReceiptEvent.UploadFileFromUri -> {
                receiptState.value.uploadedImageUri?.let {
                    if(event.isCamera) convertUriToCompressedFile(event.context, it) else
                    convertUriToFile(event.context, it)
                }
            }
            ReceiptEvent.ObserveAllReceipts -> observeAllReceipts()
            is ReceiptEvent.SetTempItemId -> setTempItemId(event.itemId)
            is ReceiptEvent.SetTempReceiptId -> setTempReceiptId(event.receiptId)
            ReceiptEvent.DeleteAllReceipts -> deleteAllReceipts()
            ReceiptEvent.DeleteItem -> {
                receiptState.value.tempItemId?.let { itemId ->
                    handleDeleteReceiptItem(itemId)
                }
            }
            ReceiptEvent.DeleteReceipt -> {
                receiptState.value.tempReceiptId?.let { receiptId ->
                    handleDeleteReceipt(receiptId)
                }
            }
            is ReceiptEvent.SelectReceipt -> handleSelectReceipt(event.receipt)
            is ReceiptEvent.ShowDeleteAllDialog -> showDeleteAllDialog(event.show)
            is ReceiptEvent.ShowDeleteItemDialog ->  showDeleteItemDialog(event.show)
            is ReceiptEvent.ShowDeleteReceiptDialog -> showDeleteReceiptDialog(event.show)
            is ReceiptEvent.ToggleReceipt -> toggleReceipt(event.receiptId)
            is ReceiptEvent.ToggleReceiptItem -> toggleReceiptItem(event.itemId)
        }
    }
    private fun showDeleteAllDialog(show: Boolean) {
        _receiptState.update {
            it.copy(
                showDeleteAllDialog = show
            )
        }
    }
    private fun showDeleteItemDialog(show: Boolean) {
        _receiptState.update {
            it.copy(
                showDeleteItemDialog = show
            )
        }
    }
    private fun handleSelectReceipt(receipt:ReceiptResponseModel) {
        _receiptState.update { it.copy(receipt = receipt, selectedReceiptId = receipt.receiptId) }
    }
    private fun showDeleteReceiptDialog(show: Boolean) {
        _receiptState.update { it.copy(
            showDeleteReceiptDialog = show
        ) }
    }
    private fun deleteAllReceipts() {
        _receiptState.update { it.copy(isProcessing = true, error = null, receipt = null) }
        viewModelScope.launch {
            receiptUseCase.deleteAllReceipts()
            _receiptState.update { it.copy(selectedReceiptId = null,  isProcessing = false) }
        }
    }
    private fun handleDeleteReceipt(receiptId: Long) {
        viewModelScope.launch {
            receiptUseCase.deleteReceiptResponseById(receiptId)
            if (_receiptState.value.selectedReceiptId == receiptId) {
                _receiptState.update {
                    it.copy(selectedReceiptId = null, receipt = null, isProcessing = false)
                }
            }
        }
    }
    private fun handleDeleteReceiptItem(receiptItemId: Long) {
        viewModelScope.launch {
            receiptUseCase.deleteReceiptItemById(receiptItemId)
        }
    }
    private fun convertUriToFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val file = uri.toFileSafely(context)
                if (file != null) {
                    _receiptState.update { it.copy(uploadedFile = file) }
                } else {
                    _receiptState.update { it.copy(error = "Failed to convert image to file") }
                }
            } catch (e: Exception) {
                _receiptState.update { it.copy(error = "Unable to read image") }
            }
        }
    }
    private fun setTempReceiptId(receiptId: Long) {
        _receiptState.update {
            it.copy(
                tempReceiptId = receiptId
            )
        }
    }
    private fun setTempItemId(itemId: Long) {
        _receiptState.update {
            it.copy(
                tempItemId = itemId
            )
        }
    }
    private fun convertUriToCompressedFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val file = uri.toCompressedFile(context)
                if (file != null) {
                    _receiptState.update { it.copy(uploadedFile = file) }
                } else {
                    _receiptState.update { it.copy(error = "Failed to convert image to file") }
                }
            } catch (e: Exception) {
                _receiptState.update { it.copy(error = "Unable to read image") }
            }
        }
    }

    private fun processReceipt() {
        log("processReceipt:")
        val file = _receiptState.value.uploadedFile ?: return
        viewModelScope.launch {
            _receiptState.update { it.copy(isProcessing = true, error = null, receipt = null,selectedReceiptId = null,) }
            try {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val multipart = MultipartBody.Part.createFormData("file", file.name, requestFile)

                log("Uploading file: ${file.absolutePath}", tag = "FileCheck")
                log("Exists: ${file.exists()}, Length: ${file.length()}", tag = "FileCheck")
                log(
                    "MD5: ${file.md5()}, name=${file.name}, size=${file.length()}",
                    tag = "FileCheck"
                )
                log(
                    "Multipart: name=${multipart.headers}, body size=${file.length()}",
                    tag = "FileCheck"
                )
                log(
                    """
                        Sending Multipart:
                        ├─ Name: file
                        ├─ Filename: ${file.name}
                        ├─ Size: ${file.length()} bytes
                        ├─ MD5: ${file.md5()}
                        └─ MIME: image/jpeg
                    """.trimIndent(),
                    tag = "FileCheck"
                )

                val result = receiptUseCase.uploadReceipt(multipart)
                log("result: $result")
                _receiptState.update { it.copy(receipt = if(result.isNotEmpty()) result[0] else null,
                    selectedReceiptId = if(result.isNotEmpty()) result[0].receiptId else null,
                    isProcessing = false, error = null) }
            } catch (e: Exception) {
                _receiptState.update {
                    it.copy(
                        error = e.message ?: "Upload failed",
                        isProcessing = false
                    )
                }
            }
        }
    }

    private fun observeAllReceipts() {
        receiptJob?.cancel()
        receiptJob = viewModelScope.launch {
            receiptUseCase.getAllReceiptResponses().collect { receipts ->
                val receipt = receipts.firstOrNull{ it.receiptId == receiptState.value.selectedReceiptId }
                _receiptState.update { it.copy(receipts = receipts, receipt = receipt) }
            }
        }
    }
    private fun retryProcessing(context: Context) {
        log("retryProcessing:")
        val currentFile = _receiptState.value.uploadedFile
        val currentUri = _receiptState.value.uploadedImageUri
        if (currentFile == null || !currentFile.exists()) {
            if (currentUri != null) {
                onEvent(ReceiptEvent.UploadUri(currentUri)) // Re-convert the file
                onEvent(ReceiptEvent.UploadFileFromUri(context)) // Re-convert the file
                onEvent(ReceiptEvent.Process)
            } else {
                _receiptState.update { it.copy(error = "No file or URI available to retry") }
            }
        } else {
            onEvent(ReceiptEvent.Process)
        }
    }
}*/
