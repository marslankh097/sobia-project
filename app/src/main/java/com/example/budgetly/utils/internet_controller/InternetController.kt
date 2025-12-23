package com.example.budgetly.utils.internet_controller

import kotlinx.coroutines.flow.Flow

interface InternetController {
    fun observe(): Flow<Status>
    enum class Status {
        Available, Unavailable, Losing, Lost
    }
    val isInternetConnected: Boolean
    val isInternetStable: Boolean
    val isInternetAvailable: Boolean
}