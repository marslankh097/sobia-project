package com.example.budgetly.ui.pinecone.state

data class PineConeState(
    val query: String = "",
    val uiState: PineConeUiState = PineConeUiState.Idle
)

