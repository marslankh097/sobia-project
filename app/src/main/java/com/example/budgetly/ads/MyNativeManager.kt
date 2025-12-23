package com.example.budgetly.ads
import android.app.Activity
import androidx.lifecycle.Lifecycle
import com.demo.budgetly.R
import com.monetization.adsmain.commons.getAdController
import com.monetization.adsmain.commons.loadAd
import com.monetization.adsmain.commons.sdkNativeAd
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.core.controllers.AdsControllerBaseHelper
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.ShimmerInfo
import com.monetization.nativeads.AdmobNativeAdsManager
import javax.inject.Singleton

@Singleton
class MyNativeManager {
    fun preLoad(placementKey: String, context: Activity, key: String) {
        key.loadAd(placementKey = placementKey, activity = context)
    }
    //for xml
      fun showNativeAd(
        context: Activity,
        adKey: String,
        adFrame: AdsUiWidget,
        lifecycle: Lifecycle,
        placementKey: String,
        layout: NativeLayouts,
        showNewAdEveryTime: Boolean = true,
        requestNewOnShow: Boolean = false,
        adWidgetData: AdsWidgetData? = null,
        screenName: String,
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
        adFrame.sdkNativeAd(
            activity = context,
            adLayout = layout.layout,
            adKey = finalAdKey,
            placementKey = placementKey,
            lifecycle = lifecycle,
            showShimmerLayout = ShimmerInfo.GivenLayout(shimmerColor = "#C2C2C2",
                listOf(R.id.ad_call_to_action,R.id.ad_app_icon,R.id.tv_ad,R.id.text_layout,R.id.ad_media)),
            adsWidgetData = adWidgetData,
            requestNewOnShow = requestNewOnShow,
            showNewAdEveryTime = showNewAdEveryTime
        )
    }
}
/*
enum class NativeLayouts(
    val layout: String,
    val shimmer: Int,
) {
    SmallButtonNative("small_button_native_layout", R.layout.small_button_native_layout),
    SmallNative("small_native_layout", R.layout.small_native_layout),
    SplitNative("jazz_native_layout", R.layout.jazz_native_layout),
    LargeNative("large_native_layout", R.layout.large_native_layout)
}

fun setShimmerColorOnView(
    context: Context,
    layoutId: Int,
): View? {
    val shimmerView = LayoutInflater.from(context).inflate(layoutId, null)
    try {
        val shimmerColor =
            ContextCompat.getColor(context, video.downloader.bannerads.R.color.shimmerColor)
        val adButton:AppCompatButton? = shimmerView.findViewById(R.id.ad_call_to_action)
        val icon:ImageView? = shimmerView.findViewById(R.id.ad_app_icon)
        val adText:TextView? = shimmerView.findViewById(R.id.tv_ad)
        val textLayout:View? = shimmerView.findViewById(R.id.text_layout)
        val mediaView: AdmobNativeMediaView? = shimmerView.findViewById(R.id.ad_media)
        mediaView?.setBackgroundColor(shimmerColor)
        adButton?.background = ContextCompat.getDrawable(context, R.drawable.shimmer_ad_btn)
        icon?.setBackgroundColor(shimmerColor)
        textLayout?.setBackgroundColor(shimmerColor)
        adText?.setBackgroundColor(shimmerColor)
        return shimmerView
    } catch (ignore: Exception) {
        return shimmerView
    }
}*/
