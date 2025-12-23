package com.example.budgetly.ui.banking.requisition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.domain.usecases.api_usecases.banking.AgreementUseCases
import com.example.budgetly.domain.usecases.api_usecases.banking.RequisitionUseCases
import com.example.budgetly.ui.banking.requisition.events.RequisitionEvent
import com.example.budgetly.ui.banking.requisition.state.RequisitionUiState
import com.example.budgetly.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- VIEWMODEL ---
@HiltViewModel
class RequisitionViewModel @Inject constructor(
    private val requisitionUseCases: RequisitionUseCases,
    private val agreementUseCases: AgreementUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<RequisitionUiState>(RequisitionUiState.Idle)
    val uiState: StateFlow<RequisitionUiState> = _uiState.asStateFlow()

    fun onEvent(event: RequisitionEvent) {
        when (event) {
            is RequisitionEvent.CreateRequisition -> createRequisition(event.institutionId, event.redirectUrl)
            is RequisitionEvent.FetchAccounts -> fetchAccountsFromRequisition(event.requisitionId)
            RequisitionEvent.Reset -> _uiState.value = RequisitionUiState.Idle
        }
    }

    private fun createRequisition(institutionId: String, redirectUrl: String) {
        viewModelScope.launch {
            _uiState.value = RequisitionUiState.Loading
            try {
                val agreementId = agreementUseCases.createEndUserAgreement.invoke(institutionId).id
                log("agreementId:$agreementId")
                val (requisition, isCached) = requisitionUseCases.createRequisition(institutionId, redirectUrl,agreementId)
                log("requisition:$requisition.... isCached $isCached")
                _uiState.value = RequisitionUiState.Success(requisition,isCached)
            } catch (e: Exception) {
                _uiState.value = RequisitionUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun fetchAccountsFromRequisition(requisitionId: String) {
        viewModelScope.launch {
            _uiState.value = RequisitionUiState.Loading
            try {
                val accounts = requisitionUseCases.getAccountsFromRequisition(requisitionId)
                _uiState.value = RequisitionUiState.AccountList(accounts)
            } catch (e: Exception) {
                _uiState.value = RequisitionUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
