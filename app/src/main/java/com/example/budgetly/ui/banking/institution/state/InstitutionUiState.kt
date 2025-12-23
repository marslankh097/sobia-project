package com.example.budgetly.ui.banking.institution.state

sealed class InstitutionUiState {
    object Idle : InstitutionUiState()
    object Loading : InstitutionUiState()
    data class Success(val institutions: List<com.example.budgetly.domain.models.api.banking.institiution.InstitutionModel>) : InstitutionUiState()
    data class Failure(val message: String) : InstitutionUiState()
}
