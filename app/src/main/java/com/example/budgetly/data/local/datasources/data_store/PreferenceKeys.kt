package com.example.budgetly.data.local.datasources.data_store
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val isDarkMode = booleanPreferencesKey("is_dark_mode")
    val isAppPurchased = booleanPreferencesKey("is_app_purchased")
    val isFirstSession = booleanPreferencesKey("is_first_session")
    val appThemePosition = intPreferencesKey("app_theme_position")
    val selectedAccountId = longPreferencesKey("selected_account_id")
    val selectedDuration = stringPreferencesKey("selected_duration")
    val fromTimeStamp = longPreferencesKey("from_time_stamp")
    val toTimeStamp = longPreferencesKey("to_time_stamp")
    val sortBy = stringPreferencesKey("sort_by")
    val orderBy = stringPreferencesKey("order_by")
}
