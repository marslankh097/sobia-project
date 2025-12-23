package com.example.budgetly.ui.banking.institution

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.domain.usecases.api_usecases.banking.InstitutionUseCases
import com.example.budgetly.ui.banking.institution.events.InstitutionEvents
import com.example.budgetly.ui.banking.institution.state.InstitutionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstitutionViewModel @Inject constructor(
    private val institutionUseCases: InstitutionUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow<InstitutionUiState>(InstitutionUiState.Idle)
    val uiState: StateFlow<InstitutionUiState> = _uiState

    init {
        onEvent(InstitutionEvents.LoadInstitutions)
    }

    fun onEvent(event: InstitutionEvents) {
        when (event) {
            InstitutionEvents.LoadInstitutions -> loadInstitutions()
            is InstitutionEvents.SelectInstitution -> {
                _uiState.value = InstitutionUiState.Idle
            }
        }
    }
    private fun loadInstitutions() {
        viewModelScope.launch {
            _uiState.value = InstitutionUiState.Loading
            try {
                val result = institutionUseCases.getInstitutions()
                _uiState.value = InstitutionUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = InstitutionUiState.Failure(e.message ?: "Something went wrong")
            }
        }
    }
}
