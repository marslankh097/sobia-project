package com.example.budgetly.data.remote.datasources.remoteConfig

interface RemoteConfigDataSource {
    suspend fun fetchAndActivate(onFetched: () -> Unit)
    fun <T> getValue(key: String, default: T): T
}
