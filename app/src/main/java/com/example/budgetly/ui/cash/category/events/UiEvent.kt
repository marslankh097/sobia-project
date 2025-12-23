package com.example.budgetly.ui.cash.category.events

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class Saved(val message: String) : UiEvent()
}
