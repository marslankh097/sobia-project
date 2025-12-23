package com.example.budgetly.ads
import com.monetization.adsmain.commons.addNewController
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.listeners.ControllersListener

object AdsEntryManager {
    fun initControllers(onAnalytics: (event: String) -> Unit) {
        val controllersListener = object : ControllersListener {
            override fun onAdLoaded(
                adKey: String,
                adType: AdType,
                placementKey: String,
                dataMap: HashMap<String, String>,
                mediationClassName: String?
            ) {
                super.onAdLoaded(adKey, adType, placementKey, dataMap,mediationClassName)
                onAnalytics.invoke("${adKey}_ad_loaded")
            }

            override fun onAdImpression(
                adKey: String,
                adType: AdType,
                placementKey: String,
                dataMap: HashMap<String, String>
            ) {
                super.onAdImpression(adKey, adType, placementKey, dataMap)
                val screenName = dataMap.get("screenName") ?: ""
                if (screenName.isNotBlank()) {
                    onAnalytics.invoke("${adKey}_${screenName}_ad_impression")
                } else {
                    onAnalytics.invoke("${adKey}_ad_impression")
                }
            }

            override fun onAdRequested(
                adKey: String,
                adType: AdType,
                placementKey: String,
                dataMap: HashMap<String, String>
            ) {
                super.onAdRequested(adKey, adType, placementKey, dataMap)
                onAnalytics.invoke("${adKey}_ad_requested")
            }

            override fun onAdFailed(
                adKey: String,
                adType: AdType,
                message: String,
                error: Int,
                placementKey: String,
                dataMap: HashMap<String, String>
            ) {
                super.onAdFailed(adKey, adType, message, error, placementKey, dataMap)
                onAnalytics.invoke("${adKey}_ad_failed_${error}")
            }
        }
        AdKeys.entries.forEach { entry ->
            addNewController(
                adKey = entry.name,
                adType = entry.adType,
                adIdsList = entry.adIds,
                listener = controllersListener,
                bannerAdType = entry.bannerAdType
            )
        }
    }
}