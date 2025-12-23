package com.example.budgetly.ads
import android.app.Activity
import androidx.compose.runtime.Composable
import com.monetization.adsmain.commons.getAdController
import com.monetization.bannerads.AdmobBannerAdsManager
import com.monetization.composeviews.SdkBanner
import com.monetization.core.controllers.AdsControllerBaseHelper
import com.monetization.core.ui.ShimmerInfo

@Composable
fun BannerAd(
    context: Activity,
    adKey: String,
    placementKey: String,
    screenName:String,
    showNewAdEveryTime: Boolean = true,
    requestNewOnShow: Boolean = false,
    hideShimmerOnFailure: Boolean = true,
) {
    val availableAds = AdmobBannerAdsManager.getAllController().filter {
        it.isAdAvailable()
    }
    val finalAdKey = if (availableAds.isNotEmpty()) {
        availableAds[0].getAdKey()
    } else {
        adKey
    }
    val controller = finalAdKey.getAdController() as? AdsControllerBaseHelper
    controller?.setDataMap(
        hashMapOf(
            "screenName" to screenName
        )
    )
    SdkBanner(
        activity = context,
        adKey = adKey,
        showShimmerLayout = ShimmerInfo.GivenLayout(hideShimmerOnFailure = hideShimmerOnFailure),
        placementKey = placementKey,
        showNewAdEveryTime = showNewAdEveryTime,
        requestNewOnShow = requestNewOnShow
    )
}