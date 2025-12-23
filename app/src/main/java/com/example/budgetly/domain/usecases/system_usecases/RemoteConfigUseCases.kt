package com.example.budgetly.domain.usecases.system_usecases

import com.example.budgetly.domain.repositories.system.RemoteConfigRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class RemoteConfigUseCases @Inject constructor(
    val fetchAndActivate: FetchAndActivateRemoteConfig,
    val getRemoteConfigValue: GetRemoteConfigValue
) //is me bhi hilt lagana? yes //ok? no wapis jao jahn ab //ok? yes
@Singleton
class FetchAndActivateRemoteConfig @Inject constructor(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(onFetched: () -> Unit) {
        return repository.fetchAndActivate(onFetched)
    }
}
@Singleton
class GetRemoteConfigValue @Inject constructor(
    private val repository: RemoteConfigRepository
) {
    operator fun <T> invoke(key: String, default: T): T {
        return repository.getValue(key, default)
    }
}
