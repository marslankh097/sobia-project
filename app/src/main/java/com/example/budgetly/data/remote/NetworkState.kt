package com.example.budgetly.data.remote

sealed class NetworkState<out T> {
    object Idle : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
    data class Success<out T>(val data: T) : NetworkState<T>()
    data class Error(val message: String) : NetworkState<Nothing>()
}
