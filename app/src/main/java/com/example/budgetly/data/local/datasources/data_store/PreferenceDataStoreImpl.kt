package com.example.budgetly.data.local.datasources.data_store
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppPreferences")
@Singleton
class PreferencesDataStoreImpl @Inject constructor(private val context: Context) :
    PreferenceDataStore {
    override suspend fun <T> saveObject(key: String, value: T, clazz: Class<T>) {
        val gson = Gson()
        val jsonString = gson.toJson(value, clazz)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = jsonString
        }
    }

    override suspend fun <T> getObject(key: String, clazz: Class<T>): Flow<T?> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[stringPreferencesKey(key)]
            if (jsonString != null) {
                val gson = Gson()
                gson.fromJson(jsonString, clazz)
            } else {
                null
            }
        }
    }

    override suspend fun <T> updateValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
    override fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}