package com.example.budgetly.data.repositories_impl.system_repo_impl

import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigDataSource
import com.example.budgetly.domain.repositories.system.RemoteConfigRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource
) : RemoteConfigRepository {

    override suspend fun fetchAndActivate(onFetched: () -> Unit) {
        return remoteConfigDataSource.fetchAndActivate(onFetched)
    }

    override fun <T> getValue(key: String, default: T): T {
        return remoteConfigDataSource.getValue(key, default)
    }
}
