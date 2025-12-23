package com.example.budgetly.data.remote.datasources.remoteConfig

import android.util.Log
import com.demo.budgetly.R
import com.example.budgetly.ads.NativeLayouts
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.AccountNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.AssistantHistoryNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.AssistantNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.BankingNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.ExpenseNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.GraphNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.IncomeNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.MainNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.PieChartNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.TestRemoteConfigCheck
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.TransactionNativeLayoutKey
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.consentTime
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.firstSplashAdType
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.mainCounterMax
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.mainCounterMin
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.returningSplashAdType
import com.example.budgetly.data.remote.datasources.remoteConfig.AdUtils.splashTime
import com.monetization.core.ad_units.core.AdType
import com.remote.firebaseconfigs.SdkRemoteConfigController
import com.remote.firebaseconfigs.listeners.SdkConfigListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteConfigDataSourceImpl @Inject constructor() : RemoteConfigDataSource {
    private var canRequestFirebaseRemoteConfig = true
    private var firebaseRemoteConfigFetchSuccessfully = false
    override suspend fun fetchAndActivate(onFetched: () -> Unit) {
        if (firebaseRemoteConfigFetchSuccessfully) {
            return
        }
        if (!canRequestFirebaseRemoteConfig) {
            return
        }
        canRequestFirebaseRemoteConfig = false
        try {
            SdkRemoteConfigController.fetchRemoteConfig(
                defaultXml = R.xml.remote_config_defaults,
                fetchOutTimeInSeconds = 8,
                handlerDelayInSeconds = 8,
                callback = object : SdkConfigListener {
                    override fun onDismiss() {
                        assignRemoteConfigs(forUpdate = false)
                        onFetched.invoke()
                        canRequestFirebaseRemoteConfig = true
                    }

                    override fun onFailure(error: String) {

                    }

                    override fun onSuccess() {
                        firebaseRemoteConfigFetchSuccessfully = true
                    }
                },
                onUpdate = {
                    assignRemoteConfigs(forUpdate = true)
                }
            )
        } catch (e: Exception) {
            canRequestFirebaseRemoteConfig = true
        }
    }

    private fun assignRemoteConfigs(forUpdate: Boolean) {
        SdkRemoteConfigController.apply {
            Log.d("configs", "assignRemoteConfigs test(forUpdate=$forUpdate): ${getRemoteConfigString("TestKey", def = "picked_default")}")
            Log.d("configs", "assignRemoteConfigs test1(forUpdate=$forUpdate): ${getRemoteConfigString("Test", def = "picked_default1")}")
            TestRemoteConfigCheck = getRemoteConfigBoolean("TestRemoteConfigCheck", def = true)
            splashTime = getRemoteConfigLong(RemoteConfigKeys.SPLASH_TIME, def = 13L)
            consentTime = getRemoteConfigLong(RemoteConfigKeys.CONSENT_TIME, def = 8L)
            mainCounterMin = getRemoteConfigLong(RemoteConfigKeys.MAIN_COUNTER_MIN, def = 2L)
            mainCounterMax = getRemoteConfigLong(RemoteConfigKeys.MAIN_COUNTER_MAX, def = 3L)

            firstSplashAdType = getRemoteConfigString(
                RemoteConfigKeys.FIRST_SPLASH_AD_TYPE,
                AdType.INTERSTITIAL.name
            )


            returningSplashAdType = getRemoteConfigString(
                RemoteConfigKeys.RETURNING_SPLASH_AD_TYPE,
                AdType.AppOpen.name
            )



            MainNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.MAIN_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            IncomeNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.INCOME_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            ExpenseNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.EXPENSE_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            BankingNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.BANKING_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )

            PieChartNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.PIE_CHART_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            AssistantNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.ASSISTANT_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            AssistantHistoryNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.ASSISTANT_HISTORY_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            TransactionNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.TRANSACTION_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            AccountNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.ACCOUNT_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
            GraphNativeLayoutKey = getRemoteConfigString(
                RemoteConfigKeys.GRAPH_NATIVE_LAYOUT,
                NativeLayouts.SplitNative.name
            )
        }
    }

    override fun <T> getValue(key: String, default: T): T {
        return try {
            when (default) {
                is String -> SdkRemoteConfigController.getRemoteConfigString(key, default)
                    .ifEmpty { default } as T

                is Boolean -> SdkRemoteConfigController.getRemoteConfigBoolean(key, default) as T
                is Long -> SdkRemoteConfigController.getRemoteConfigLong(key, default) as T
                else -> default
            }
        } catch (e: Exception) {
            default
        }
    }
}
