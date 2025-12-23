package com.example.budgetly.ui.pinecone.state

import com.example.budgetly.domain.models.api.pinecone.PineConeMessageModel

sealed interface PineConeUiState {
    object Idle : PineConeUiState
    object Loading : PineConeUiState
    data class Success(val messages: List<PineConeMessageModel>) : PineConeUiState
    data class Error(val message: String) : PineConeUiState
}
