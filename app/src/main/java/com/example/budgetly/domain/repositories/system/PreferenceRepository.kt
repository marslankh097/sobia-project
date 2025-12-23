package com.example.budgetly.domain.repositories.system

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    // preference data store
    suspend fun <T> updatePreferenceValue(key: Preferences.Key<T>, value: T)
    fun <T> getPreferenceValue(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> savePreferenceObject(key: String, value: T, clazz: Class<T>)
    suspend fun <T> getPreferenceObject(key: String, clazz: Class<T>): Flow<T?>
    suspend fun clearPreferenceDataStore()
}