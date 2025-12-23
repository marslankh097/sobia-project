package com.example.budgetly.data.local.datasources.data_store
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferenceDataStore {
    suspend fun <T> updateValue(key: Preferences.Key<T>, value: T)
    fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> saveObject(key: String, value: T, clazz: Class<T>)
    suspend fun <T> getObject(key: String, clazz: Class<T>): Flow<T?>
    suspend fun clear()
}