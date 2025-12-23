package com.example.budgetly

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.demo.budgetly.databinding.ActivityMainBinding
import com.example.budgetly.MyApplication.Companion.appContext
import com.example.budgetly.ads.AdsEntryManager
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.MyInterManager
import com.example.budgetly.domain.usecases.system_usecases.RemoteConfigUseCases
import com.example.budgetly.ui.navigation.AppNavigation
import com.example.budgetly.ui.navigation.LocalNavHostController
import com.example.budgetly.utils.BudgetAppTheme
import com.example.budgetly.utils.Utils.referenceToRequisitionId
import com.example.budgetly.utils.clearCacheDirectory
import com.example.budgetly.utils.defaultBgColor
import com.example.budgetly.utils.internet_controller.InternetController
import com.example.budgetly.utils.setAnalytics
import com.example.budgetly.utils.shared_components.SetupEdgeToEdgeBars
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    @Inject
    lateinit var coroutineScope: CoroutineScope
    @Inject
    lateinit var remoteConfigUseCases: RemoteConfigUseCases
    @Inject
    lateinit var  internetController: InternetController
    @Inject
    lateinit var  commonAdsUtil: CommonAdsUtil

    @Inject
    lateinit var  interManager: MyInterManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        coroutineScope.launch {
            clearCacheDirectory()
            if(internetController.isInternetAvailable) {
                remoteConfigUseCases.fetchAndActivate{}
            }
            AdsEntryManager.initControllers {event->
                setAnalytics(event)
            }

        }
        lifecycleScope.launch {
            (appContext as MyApplication).initOpenAppAd()
        }


        setContent {
//            val isDark = mainViewModel.getPrefsDarkMode()
            val isDark = false
            SetupEdgeToEdgeBars(this, isDark)
            BudgetAppTheme(
                isDarkMode = mainViewModel.getPrefsDarkMode(),
                themePosition = mainViewModel.getAppThemePosition()
            ){
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .imePadding()
                        .background(color = defaultBgColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .background(color = defaultBgColor)
                    ) {
                        CompositionLocalProvider(LocalNavHostController provides navController) {
                            AppNavigation(mainViewModel,commonAdsUtil, interManager)
                        }
                    }
                }
            }
        }
    }
//     Handle redirect and fetch bank data
     override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    Log.d("exception", "onNewIntent:")
        intent.data?.let { uri ->
            Log.d("exception", "intent.data.uri: $uri")
            val reference = uri.getQueryParameter("ref")
            Log.d("exception", "reference: $reference")
            if (reference != null) {
                val requisitionId = referenceToRequisitionId[reference]
                Log.d("exception", "Requisition ID received: $requisitionId")
                mainViewModel.setRequisitionId(requisitionId)
            }
        }
    }

}