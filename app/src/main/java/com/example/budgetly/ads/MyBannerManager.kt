package com.example.budgetly.ads
import android.app.Activity
import androidx.lifecycle.Lifecycle
import com.demo.budgetly.R
import com.monetization.adsmain.commons.loadAd
import com.monetization.adsmain.commons.sdkBannerAd
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.core.commons.Utils.toShimmerView
import javax.inject.Singleton

@Singleton
class MyBannerManager {
    fun preLoad(placementKey: String, context: Activity, key: String) {
        key.loadAd(placementKey = placementKey,  activity = context)
    }
    fun showBannerAds(
        context: Activity,
        adFrame: AdsUiWidget,
        lifecycle: Lifecycle,
        placementKey: String,
        showNewAdEveryTime: Boolean = false,
        key: String
    ) {
        adFrame.sdkBannerAd(
            activity = context,
            adKey = key,
            placementKey = placementKey,
            lifecycle = lifecycle,
            requestNewOnShow = false,
            showNewAdEveryTime = showNewAdEveryTime,
            showShimmerLayout = R.layout.small_button_native_layout.toShimmerView(context)
        )
    }

}