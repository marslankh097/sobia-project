package com.example.budgetly.domain.usecases.system_usecases

import androidx.datastore.preferences.core.Preferences
import com.example.budgetly.domain.repositories.system.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class PreferenceUseCases @Inject constructor(
    val updatePreferenceValue: UpdatePreferenceValue,
    val getPreferenceValue: GetPreferenceValue,
    val savePreferenceObject: SavePreferenceObject,
    val getPreferenceObject: GetPreferenceObject,
    val clearPreferenceDataStore: ClearPreferenceDataStore
)
@Singleton
class UpdatePreferenceValue @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun <T> invoke(key: Preferences.Key<T>, value: T) {
        repository.updatePreferenceValue(key, value)
    }
}
@Singleton
class GetPreferenceValue @Inject constructor(
    private val repository: PreferenceRepository
) {
    operator fun <T> invoke(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return repository.getPreferenceValue(key, defaultValue)
    }
}
@Singleton
class SavePreferenceObject @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun <T> invoke(key: String, value: T, clazz: Class<T>) {
        repository.savePreferenceObject(key, value, clazz)
    }
}
@Singleton
class GetPreferenceObject @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun <T> invoke(key: String, clazz: Class<T>): Flow<T?> {
        return repository.getPreferenceObject(key, clazz)
    }
}
@Singleton
class ClearPreferenceDataStore @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke() {
        repository.clearPreferenceDataStore()
    }
}
