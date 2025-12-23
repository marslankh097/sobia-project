package com.example.budgetly.ui.splash

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.BannerAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.splash.content.SplashProgressBar
import com.example.budgetly.ui.splash.content.SplashTopContent
import com.example.budgetly.ui.splash.state.SplashOneTimeEvent
import com.example.budgetly.ui.splash.state.SplashStep
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.defaultBgColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateNext: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showSplashBanner by remember {
        mutableStateOf(false)
    }
    // Handle one-time navigation events
    LaunchedEffect(Unit) {
        viewModel.oneTimeEvents.collect { event ->
            when (event) {
                is SplashOneTimeEvent.NavigateToMain -> {
                    Log.d("SplashViewModel", "collecting on time event")
                    navigateNext()
                }

                SplashOneTimeEvent.isAdsInitialized ->{
                    showSplashBanner = true
                }
            }
        }
    }
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.pauseTimers()
                }
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.resumeTimers()
                }
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    // Start the splash flow when screen launches
    LaunchedEffect(Unit) {
        val activity = context as? Activity
        if (activity != null) {
            viewModel.beginSplashFlow(activity, lifecycleOwner.lifecycle)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(defaultBgColor)
    ) {
        Column(modifier = Modifier.weight(1f).padding(horizontal = 12.sdp, vertical = 20.sdp)){
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SplashTopContent()
            }

            Spacer(modifier = Modifier.weight(1F))
            if(state.currentStep in listOf(SplashStep.LoadingSplashAd, SplashStep.Complete)){
                // Use ViewModel's progress state
                SplashProgressBar(
                    modifier = Modifier.fillMaxWidth(),
                    textId = R.string.loading,
                    progress = state.progress
                )
            }
            VerticalSpacer(12)
        }
        if(showSplashBanner){
            BannerAd(
                context = context as Activity,
                adKey = AdKeys.SplashBanner.name,
                placementKey = RemoteConfigKeys.SPLASH_BANNER_ENABLED,
                screenName = "Splash_Screen_Bottom"
            )
        }
        VerticalSpacer(5)
    }
}
