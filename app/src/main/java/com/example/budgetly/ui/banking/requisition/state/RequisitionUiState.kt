package com.example.budgetly.ui.banking.requisition.state

sealed class RequisitionUiState {
    object Idle : RequisitionUiState()
    object Loading : RequisitionUiState()
    data class Success(val requisition: com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel, val isCached:Boolean) : RequisitionUiState()
    data class AccountList(val accounts: List<com.example.budgetly.domain.models.api.banking.account.AccountModel>) : RequisitionUiState()
    data class Error(val message: String) : RequisitionUiState()
}
