package com.example.budgetly.domain.repositories.system

interface RemoteConfigRepository {
    suspend fun fetchAndActivate(onFetched: () -> Unit)
    fun <T> getValue(key: String, default: T): T
}
