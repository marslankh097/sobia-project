package com.example.budgetly.ads
import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.bannerads.BannerCollapsable
import com.monetization.core.ad_units.core.AdType

enum class AdKeys(val adType: AdType, val adIds: List<String>, val bannerAdType: BannerAdType? = null) {
    AppOpen(adType = AdType.AppOpen, adIds = listOf("")),
    FirstSplashAd(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    ReturningSplashAd(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    SplashBanner(adType = AdType.BANNER, adIds = listOf(""),bannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner)),


    MainInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    BankingInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    AssistantInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    NotificationInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    ReceiptInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),

    AccountsInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    IncomeInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),
    ExpenseInterstitial(adType = AdType.INTERSTITIAL, adIds = listOf("")),

    MainNative(adType = AdType.NATIVE, adIds = listOf("")),
    ReceiptNative(adType = AdType.NATIVE, adIds = listOf("")),
    MainBanner(adType = AdType.BANNER, adIds = listOf(""), bannerAdType = BannerAdType.Collapsible(BannerCollapsable.CollapseBottom)),
    IncomeNative(adType = AdType.NATIVE, adIds = listOf("")),
    ExpenseNative(adType = AdType.NATIVE, adIds = listOf("")),

    AccountNative(adType = AdType.NATIVE, adIds = listOf("")),
    TransactionNative(adType = AdType.NATIVE, adIds = listOf("")),
    GraphNative(adType = AdType.NATIVE, adIds = listOf("")),
    PieChartNative(adType = AdType.NATIVE, adIds = listOf("")),

    LocalizeNative(adType = AdType.NATIVE, adIds = listOf("")),
    BankingNative(adType = AdType.NATIVE, adIds = listOf("")),
    AIAssistantNative(adType = AdType.NATIVE, adIds = listOf("")),
    ReceiptBanner(adType = AdType.BANNER, adIds = listOf(""), bannerAdType = BannerAdType.Collapsible(BannerCollapsable.CollapseBottom)),
    NotificationBanner(adType = AdType.BANNER, adIds = listOf(""), bannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner)),
}