package com.example.budgetly.ads
import android.app.Activity
import androidx.compose.runtime.Composable
import com.monetization.adsmain.commons.getAdController
import com.monetization.composeviews.SdkNativeAd
import com.monetization.core.controllers.AdsControllerBaseHelper
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo
import com.monetization.nativeads.AdmobNativeAdsManager
@Composable
fun NativeAd(
    context: Activity,
    adKey: String,
    placementKey: String,
    layoutKey: String = NativeLayouts.SmallNative.name,
    showNewAdEveryTime: Boolean = true,
    requestNewOnShow: Boolean = false,
    hideShimmerOnFailure: Boolean = true,
    adsWidgetData: AdsWidgetData? = null,
    screenName: String,
    commonAdsUtil: CommonAdsUtil ,
) {
    val availableAds = AdmobNativeAdsManager.getAllController().filter {
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
    var nativeLayout = NativeLayouts.SmallNative
    NativeLayouts.entries.forEach {
        if (it.name == layoutKey) {
            nativeLayout = it
        }
    }
    SdkNativeAd(
        activity = context,
        adLayout = LayoutInfo.LayoutByXmlView(nativeLayout.shimmer),
        adKey = finalAdKey,
        placementKey = placementKey,
        requestNewOnShow = requestNewOnShow,
        showNewAdEveryTime = showNewAdEveryTime,
        showShimmerLayout = ShimmerInfo.ShimmerByView(
            commonAdsUtil.setShimmerColorOnView(context, nativeLayout.shimmer),
            hideShimmerOnFailure = hideShimmerOnFailure
        ),
        adsWidgetData = adsWidgetData,
    )
}