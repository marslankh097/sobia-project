package com.example.budgetly.data.remote.datasources.remoteConfig

import com.example.budgetly.ads.NativeLayouts
import com.monetization.core.ad_units.core.AdType

object AdUtils {
    var TestRemoteConfigCheck = true
    var splashTime = 13L
    var consentTime = 8L
    var mainCounterMin = 2L
    var mainCounterMax = 3L

    var firstSplashAdType = AdType.INTERSTITIAL.name
    var returningSplashAdType = AdType.AppOpen.name

    var MainNativeLayoutKey = NativeLayouts.SplitNative.name
    var IncomeNativeLayoutKey = NativeLayouts.SplitNative.name
    var ExpenseNativeLayoutKey = NativeLayouts.LargeNative.name
    var BankingNativeLayoutKey = NativeLayouts.LargeNative.name


    var PieChartNativeLayoutKey = NativeLayouts.SplitNative.name
    var AssistantNativeLayoutKey = NativeLayouts.SplitNative.name
    var AssistantHistoryNativeLayoutKey = NativeLayouts.LargeNative.name
    var AccountNativeLayoutKey = NativeLayouts.LargeNative.name
    var TransactionNativeLayoutKey = NativeLayouts.LargeNative.name
    var GraphNativeLayoutKey = NativeLayouts.LargeNative.name
}