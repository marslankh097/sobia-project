package com.example.budgetly.data.repositories_impl.system_repo_impl

import androidx.datastore.preferences.core.Preferences
import com.example.budgetly.data.local.datasources.data_store.PreferenceDataStore
import com.example.budgetly.domain.repositories.system.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val preferenceDataSource: PreferenceDataStore
) : PreferenceRepository {
    override suspend fun <T> updatePreferenceValue(key: Preferences.Key<T>, value: T) {
        preferenceDataSource.updateValue(key,value)
    }

    override fun <T> getPreferenceValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return preferenceDataSource.getValue(key,defaultValue)
    }

    override suspend fun <T> savePreferenceObject(key: String, value: T, clazz: Class<T>) {
        preferenceDataSource.saveObject(key,value,clazz)
    }

    override suspend fun <T> getPreferenceObject(key: String, clazz: Class<T>): Flow<T?> {
        return preferenceDataSource.getObject(key,clazz)
    }

    override suspend fun clearPreferenceDataStore() {
        preferenceDataSource.clear()
    }
}