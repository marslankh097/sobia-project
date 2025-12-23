package com.example.budgetly.ads
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.demo.budgetly.R
import com.example.budgetly.data.local.datasources.data_store.PreferenceKeys
import com.example.budgetly.domain.usecases.system_usecases.PreferenceUseCases
import com.example.budgetly.google_consent.GoogleConsentManager
import com.example.budgetly.utils.internet_controller.InternetController
import com.google.android.gms.ads.nativead.MediaView
import com.monetization.core.counters.CounterInfo
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterStrategies
import com.monetization.core.utils.dialog.SdkDialogs
import com.monetization.core.utils.dialog.showBlackBgDialog
import com.monetization.core.utils.dialog.showNormalLoadingDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonAdsUtil @Inject constructor(
    private val consent: GoogleConsentManager,
    private val preferenceUseCases: PreferenceUseCases,
    private val internetController: InternetController,
) {
    private var sdkDialogs: SdkDialogs? = null
    fun registerCounter(counterKey:String, start:Int = 0, max:Int = 2){
        CounterManager.createACounter(
            key = counterKey,
            info = CounterInfo(
            currentPoint = start,
            maxPoint = max,
            adNotShownStrategy = CounterStrategies.KeepSameValue,
            adShownStrategy = CounterStrategies.ResetToZero,
           )
        )
    }
    private fun isAppPurchased(): Boolean {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.isAppPurchased,false).first()
        }
    }
    fun setShimmerColorOnView(
        context: Context,
        layoutId: Int,
    ): View? {
        val shimmerView = LayoutInflater.from(context).inflate(layoutId, null)
        try {
            val shimmerColor =
                ContextCompat.getColor(context, R.color.shimmer_color)
            val adButton: AppCompatButton? = shimmerView.findViewById(R.id.ad_call_to_action)
            val icon: ImageView? = shimmerView.findViewById(R.id.ad_app_icon)
            val adText: TextView? = shimmerView.findViewById(R.id.tv_ad)
            val textLayout: View? = shimmerView.findViewById(R.id.text_layout)
            val mediaView: MediaView? = shimmerView.findViewById(R.id.ad_media)
            mediaView?.setBackgroundColor(shimmerColor)
            adButton?.background = ContextCompat.getDrawable(context, R.drawable.shimmer_ad_btn)
            icon?.setBackgroundColor(shimmerColor)
            textLayout?.setBackgroundColor(shimmerColor)
            adText?.setBackgroundColor(shimmerColor)
            return shimmerView
        } catch (ignore: Exception) {
            return shimmerView
        }
    }
    fun hideShowDialog( activity: Activity,show: Boolean, blackBgDialog: Boolean = false) {
        if (show) {
            sdkDialogs?.hideLoadingDialog()
            sdkDialogs = SdkDialogs(activity)
            if (blackBgDialog) {
                sdkDialogs?.showBlackBgDialog()
            } else {
                sdkDialogs?.showNormalLoadingDialog()
            }
        } else {
            sdkDialogs?.hideLoadingDialog()
        }
    }

    fun canLoadAd(): Boolean {
        return consent.canRequestAds && isAppPurchased().not() && internetController.isInternetConnected
    }

    fun canShowAd(): Boolean {
        return consent.canRequestAds && isAppPurchased().not()
    }
}