package com.example.budgetly.ui.on_boarding
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.BannerAd
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.MyInterManager
import com.example.budgetly.ads.NativeAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.on_boarding.content.OnBoardingCard
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.defaultBgColor
import com.example.budgetly.utils.dialog.AppExitDialog
import com.example.budgetly.utils.log
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.TopBar
import ir.kaaveh.sdpcompose.sdp
import kotlin.system.exitProcess

@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier, navigateToBanking:()->Unit, navigateToCash:()->Unit,
                     navigateToAssistant:()->Unit,
                     navigateToCameraGallery:()->Unit,
                     navigateToNotificationListener:()->Unit,
                     commonAdsUtil: CommonAdsUtil,
                     interManager: MyInterManager,
                     navigateBack:()->Unit) {
    var showExitDialog by remember {
        mutableStateOf(false)
    }
    val handleBack = {
        showExitDialog = true
    }
    val context = LocalContext.current
    if(showExitDialog){
        log("showExitDialog:$showExitDialog")
        AppExitDialog(
            title = stringResource(R.string.exit),
            msg = stringResource(R.string.are_you_sure_you_want_to_exit_the_app),
            positiveText = stringResource(R.string.exit),
            negativeText = stringResource(R.string.cancel),
            onButtonClick = {
                if (it) {
                    exitProcess(0)
                }
                showExitDialog = false
            })
    }
    fun showMainClickInterstitial(onAdDismiss: (Boolean) -> Unit){
        interManager.showInterstitial(
            activity = context as Activity,
            placementKey = RemoteConfigKeys.MAIN_FEATURE_AD_ENABLED,
            adKey = AdKeys.MainInterstitial.name,
            counterKey = RemoteConfigKeys.MAIN_SCREEN_COUNTER,
            preloadJustBeforeCounterReached = true,
            screenName = "Main_Screen_Feature_Click",
            onAdDismiss = onAdDismiss
        )
    }
    BackHandler { handleBack() }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(defaultBgColor)
    ) {
        TopBar("AI Budgetly") {
            handleBack()
        }
        Column(modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState(), true)
            .background(secondaryBgColor)
            .padding(12.sdp), verticalArrangement = Arrangement.spacedBy(20.sdp)) {
            OnBoardingCard(imgId = R.drawable.img_bank, title = "Banking", subTitle = "Explore Banking Transactions") {
                showMainClickInterstitial{ navigateToBanking.invoke() }
            }
            OnBoardingCard(imgId = R.drawable.img_cash, title = "Cash", subTitle = "Check Manual Cash Transactions") {
                showMainClickInterstitial{
                    navigateToCash.invoke()
                }
            }
            NativeAd(
                context = context as Activity,
                adKey = AdKeys.MainNative.name,
                placementKey = RemoteConfigKeys.MAIN_NATIVE_ENABLED,
                layoutKey = RemoteConfigKeys.MAIN_NATIVE_LAYOUT,
                screenName = "Main_On_Boarding_Screen_Middle",
                commonAdsUtil = commonAdsUtil
            )
            OnBoardingCard(imgId = R.drawable.img_ai_assistant, title = "AI Assistant", subTitle = "Talk to your Personal Financial Assistant") {
                showMainClickInterstitial {
                    navigateToAssistant.invoke()
                }
            }
            OnBoardingCard(imgId = R.drawable.img_upload_receipts, title = "Upload Receipts", subTitle = "Upload and Read your Receipts") {
                showMainClickInterstitial {
                    navigateToCameraGallery.invoke()
                }
            }
            OnBoardingCard(imgId = R.drawable.img_notifcation_listener, title = "Notification Listener", subTitle = "Listen and Capture All SMS & Email Notifications") {
                showMainClickInterstitial {
                    navigateToNotificationListener.invoke()
                }
            }
        }
        VerticalSpacer()
        BannerAd(
            context = context as Activity,
            adKey = AdKeys.MainBanner.name,
            placementKey = RemoteConfigKeys.MAIN_BANNER_ENABLED,
            screenName = "Main_On_Boarding_Screen_Bottom"
        )
        VerticalSpacer(5)
    }
}


