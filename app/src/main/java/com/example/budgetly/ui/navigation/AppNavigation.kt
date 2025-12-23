package com.example.budgetly.ui.navigation
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.budgetly.MainViewModel
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.MyInterManager
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.assistant.AssistantViewModel
import com.example.budgetly.ui.assistant.screens.chat.AssistantScreen
import com.example.budgetly.ui.assistant.screens.chat_history.AssistantHistoryScreen
import com.example.budgetly.ui.banking.institution.InstitutionScreen
import com.example.budgetly.ui.banking.requisition.RequisitionFlowScreen
import com.example.budgetly.ui.banking.transactions.TransactionDisplayScreen
import com.example.budgetly.ui.cash.accounts.AccountsViewModel
import com.example.budgetly.ui.cash.accounts.screens.account_insert.AccountInsertUpdateScreen
import com.example.budgetly.ui.cash.accounts.screens.account_selection.AccountSelectionScreen
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.screens.category_display.CategoryDisplayScreen
import com.example.budgetly.ui.cash.category.screens.category_insert.CategoryInsertUpdateScreen
import com.example.budgetly.ui.cash.category.screens.parent_category_selection.CategorySelectionScreen
import com.example.budgetly.ui.cash.filter.screens.FilterScreen
import com.example.budgetly.ui.cash.home.screens.home.MainHomeScreen
import com.example.budgetly.ui.cash.home.screens.pie_chart.PieChartDetailScreen
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.screens.date_time_picker.DateTimePickerScreen
import com.example.budgetly.ui.cash.transaction.screens.transaction_display.TransactionsDisplayScreen
import com.example.budgetly.ui.cash.transaction.screens.transaction_insert.TransactionInsertScreen
import com.example.budgetly.ui.notification_listener.NotificationViewModel
import com.example.budgetly.ui.notification_listener.screen.NotificationScreen
import com.example.budgetly.ui.notification_listener.screen.NotificationSettingsScreen
import com.example.budgetly.ui.on_boarding.OnBoardingScreen
import com.example.budgetly.ui.pinecone.PineConeChatViewModel
import com.example.budgetly.ui.pinecone.screens.PineConeScreen
import com.example.budgetly.ui.splash.SplashScreen
import com.example.budgetly.ui.upload_receipt.ReceiptViewModel
import com.example.budgetly.ui.upload_receipt.screens.camera_gallery.CameraGalleryScreen
import com.example.budgetly.ui.upload_receipt.screens.receipt_history.ReceiptHistoryScreen
import com.example.budgetly.ui.upload_receipt.screens.receipt_processing.ReceiptProcessingScreen
import com.example.budgetly.ui.upload_receipt.screens.receipt_result.ReceiptResultScreen
import com.example.budgetly.utils.log
import com.example.budgetly.utils.toDateString

@Composable
fun AppNavigation(mainViewModel:MainViewModel = hiltViewModel(), commonAdsUtil: CommonAdsUtil,interManager: MyInterManager) {
    val navController = LocalNavHostController.current
    val requisitionId by mainViewModel.requisitionId.collectAsState()
//    var initialized by rememberSaveable { mutableStateOf(false) }
    val appNavigation = remember {
        AppNavigationActions(navController)
    }
    val accounts by mainViewModel.accounts.collectAsState()

//    LaunchedEffect(accounts) {
//        if (!initialized && accounts.isNotEmpty()) {
//            initialized = true
////            transactionViewModel.onEvent(TransactionEvent.SetAccount(accounts.first()))
//            accountsViewModel.onEvent(AccountEvent.SetSelectedAccount(accounts.first()))
//        }
//    }
    // Observe deep link and navigate if ID is available
    LaunchedEffect(requisitionId) {
        requisitionId?.let { id ->
            appNavigation.navigateToAccountsScreen(id)
            mainViewModel.setRequisitionId(null)
        }
    }
    val startRoute by remember {
        mutableStateOf(
//           Screens.SplashScreen.name
           Screens.PresentationMainScreen.name
        )
    }
    val context = LocalContext.current
    fun showMainFeatureBackInterstitial(placementKey:String,adKey: String, screenName:String, onAdDismiss: (Boolean) -> Unit){
        interManager.showInterstitial(
            activity = context as Activity,
            placementKey = placementKey,
            adKey = adKey,
            counterKey = RemoteConfigKeys.MAIN_SCREEN_COUNTER,
            preloadJustBeforeCounterReached = true,
            screenName = screenName,
            onAdDismiss = onAdDismiss
        )
    }
    NavHost(
        navController = navController, startDestination = startRoute
    ) {
        composable(Screens.SplashScreen.name) {
            SplashScreen(
                navigateNext = {
                    interManager.registerMainCounter(counterKey = RemoteConfigKeys.MAIN_SCREEN_COUNTER)
                    appNavigation.navigateToOnBoardingScreen()
                }
            )
        }
        composable(Screens.OnBoardingScreen.name) {
            OnBoardingScreen(
                navigateToBanking = appNavigation.navigateToInstitutionScreen,
                 navigateToCash = appNavigation.navigateToMainHomeScreen,
                navigateToAssistant = appNavigation.navigateToAssistantScreen,
                navigateToCameraGallery = appNavigation.navigateToCameraGallery,
                navigateToNotificationListener = appNavigation.navigateToNotificationScreen,
                navigateBack = appNavigation.navigateBack,
                commonAdsUtil = commonAdsUtil,
                interManager = interManager
            )
        }

        composable(Screens.NotificationScreen.name) {
            val notificationViewModel: NotificationViewModel = hiltViewModel(it)
            NotificationScreen(
                navigateBack ={
                    showMainFeatureBackInterstitial(
                        placementKey = RemoteConfigKeys.NOTIFICATION_BACK_AD_ENABLED, adKey = AdKeys.NotificationInterstitial.name,
                        screenName = "Notification_Screen_BackPress",
                        onAdDismiss = {
                            appNavigation.navigateBack()
                        }
                    )
                },
                notificationViewModel = notificationViewModel,
                navigateToNotificationSetting = {
                    appNavigation.navigateToNotificationSettingsScreen()
                }
            )
        }
        composable(Screens.NotificationSettingScreen.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.NotificationScreen.name)
            }
            val notificationViewModel = hiltViewModel<NotificationViewModel>(parentEntry)
            NotificationSettingsScreen(
                navigateBack = appNavigation.navigateBack,
                notificationViewModel = notificationViewModel
            )
        }
        composable(Screens.CameraGalleryScreen.name) {
            val receiptViewModel: ReceiptViewModel = hiltViewModel(it)
            CameraGalleryScreen(
                receiptViewModel = receiptViewModel,
                onImagePicked = {
                    appNavigation.navigateToReceiptProcessing()
                },
                navigateToReceiptHistory = {
                    appNavigation.navigateToReceiptHistory()
                },
                navigateBack = {
                    showMainFeatureBackInterstitial(
                        placementKey = RemoteConfigKeys.RECEIPT_BACK_AD_ENABLED, adKey = AdKeys.ReceiptInterstitial.name,
                        screenName = "Receipt_Screen_BackPress",
                        onAdDismiss = {
                            appNavigation.navigateBack()
                        }
                    )
                }
            )
        }
        composable(Screens.ReceiptHistoryScreen.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.CameraGalleryScreen.name)
            }
            val receiptViewModel = hiltViewModel<ReceiptViewModel>(parentEntry)
            ReceiptHistoryScreen(
                receiptViewModel = receiptViewModel,
                commonAdsUtil = commonAdsUtil,
                navigateToReceiptResult = {
                    appNavigation.navigateToReceiptResult()
                },
                navigateBack = {
                    appNavigation.navigateBack()
                }
            )
        }
        composable(Screens.ReceiptProcessingScreen.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.CameraGalleryScreen.name)
            }
            val receiptViewModel = hiltViewModel<ReceiptViewModel>(parentEntry)
            ReceiptProcessingScreen(
                receiptViewModel = receiptViewModel,
                navigateToReceiptResult = appNavigation.navigateToReceiptResult,
                navigateBack = {
                    appNavigation.navigateBack()
                }
            )
        }
        composable(Screens.ReceiptResultScreen.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.CameraGalleryScreen.name)
            }
            val receiptViewModel = hiltViewModel<ReceiptViewModel>(parentEntry)
            ReceiptResultScreen(
                receiptViewModel = receiptViewModel,
                commonAdsUtil = commonAdsUtil,
                navigateBack = {
                    appNavigation.navigateBack()
                }
            )
        }


        composable(Screens.AssistantScreen.name) {
            val assistantViewModel: AssistantViewModel = hiltViewModel(it)
            AssistantScreen(
                navigateToAssistantHistory = appNavigation.navigateToAssistantHistoryScreen,
                assistantViewModel = assistantViewModel, commonAdsUtil = commonAdsUtil,
              navigateBack = {
                  showMainFeatureBackInterstitial(
                      placementKey = RemoteConfigKeys.ASSISTANT_BACK_AD_ENABLED, adKey = AdKeys.AssistantInterstitial.name,
                      screenName = "Assistant_Screen_BackPress",
                      onAdDismiss = {
                          appNavigation.navigateBack()
                      }
                  )
              }
            )
        }
        composable(Screens.AssistantHistoryScreen.name) {
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.AssistantScreen.name)
            }
            val assistantViewModel = hiltViewModel<AssistantViewModel>(parentEntry)
            AssistantHistoryScreen(
                navigateToAssistantScreen = appNavigation.navigateToAssistantScreen,
                assistantViewModel = assistantViewModel,
                commonAdsUtil = commonAdsUtil,
              navigateBack = {
                  appNavigation.navigateBack()
              }
            )
        }
        composable(Screens.PineConeScreen.name) {
            val chatViewModel:PineConeChatViewModel = hiltViewModel(it)
            PineConeScreen(
                chatViewModel = chatViewModel,
                navigateBack = {
                    appNavigation.navigateBack()
                }
            )
        }

        //banking api screens
        composable(Screens.InstitutionScreen.name ) {
            InstitutionScreen (
                onInstitutionSelected = { institutionId->
                    appNavigation.navigateToRequisitionScreen.invoke(institutionId)
                },
                navigateBack = {
                    showMainFeatureBackInterstitial(
                        placementKey = RemoteConfigKeys.BANKING_BACK_AD_ENABLED, adKey = AdKeys.BankingInterstitial.name,
                        screenName = "Banking_Screen_BackPress",
                        onAdDismiss = {
                            appNavigation.navigateBack()
                        }
                    )
                }
            )
        }

        composable(Screens.RequisitionScreen.name + "/{institutionId}") {backStackEntry->
            val institutionId = backStackEntry.arguments?.getString("institutionId") ?: ""
            RequisitionFlowScreen(
                institutionId = institutionId,
                redirectUrl = "https://abdurrehman4838.github.io/nordigen-redirect/",
                navigateToAccounts = { requisitionId->
                    appNavigation.navigateToAccountsScreen(requisitionId)
                },
                navigateBack = appNavigation.navigateBack
            )
        }
        composable(Screens.AccountsScreen.name + "/{requisitionId}") { backStackEntry->
            val reqId = backStackEntry.arguments?.getString("requisitionId") ?: ""
            RequisitionFlowScreen(
                requisitionId = reqId,
                fromIntent = true,
                onAccountClicked = {accountId->
                    appNavigation.navigateToTransactionScreen.invoke(accountId)
                },
                navigateBack = appNavigation.navigateBack
            )
        }
        composable(Screens.TransactionScreen.name + "/{accountId}") { backStackEntry->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            TransactionDisplayScreen(
                accountId = accountId,
                navigateBack = appNavigation.navigateBack
            )
        }

        //cash screens
        composable(Screens.MainHomeScreen.name) {
            val accountsViewModel = hiltViewModel<AccountsViewModel>(it)
            val categoryViewModel = hiltViewModel<CategoryViewModel>(it)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(it)
            MainHomeScreen(
                commonAdsUtil = commonAdsUtil,
                navigateBack = {
                    showMainFeatureBackInterstitial(
                        placementKey = RemoteConfigKeys.CASH_BACK_AD_ENABLED, adKey = AdKeys.AccountsInterstitial.name,
                        screenName = "Main_Cash_Screen_BackPress",
                        onAdDismiss = {
                            appNavigation.navigateBack()
                        }
                    )
                },
                navigateToCategoryDisplay = {
                    appNavigation.navigateToCategoryDisplayScreen(it)
                },
                navigateToAccountSelection ={
                    appNavigation.navigateToAccountsSelectionScreen()
                } ,
                navigateToTransactionDisplay = {
                    appNavigation.navigateToTransactionsDisplayScreen()
                },
                navigateToFilterScreen = {
                    appNavigation.navigateToFilterScreen()
                },
                onChartClick = {
                    appNavigation.navigateToPieChartDetailScreen(it, false)
                },
                categoryViewModel = categoryViewModel,
                transactionViewModel = transactionViewModel,
                accountsViewModel = accountsViewModel
            )
        }
        composable(Screens.PieChartDetailScreen.name + "/{isExpense}/{isSubcategoryView}") { backStackEntry ->
            val isExpense = backStackEntry.arguments?.getString("isExpense")?.toBoolean() ?: true
            val isSubcategoryView = backStackEntry.arguments?.getString("isSubcategoryView")?.toBoolean() ?: false
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            PieChartDetailScreen(
                isExpense = isExpense,
                isSubcategoryView = isSubcategoryView,
                categoryViewModel = categoryViewModel,
                transactionsViewModel = transactionViewModel,
                navigateToSubCategoryPieChart = {
                    appNavigation.navigateToPieChartDetailScreen(it, true)
                },
                commonAdsUtil = commonAdsUtil,
                navigateBack = { appNavigation.navigateBack() }
            )
        }
        //category screens
        composable(Screens.CategoryDisplayScreen.name + "/{isExpense}") {backStackEntry->
            val isExpense = backStackEntry.arguments?.getString("isExpense")?.toBooleanStrictOrNull()?:false
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            CategoryDisplayScreen(
                isExpense = isExpense,
                navigateBack = appNavigation.navigateBack,
                navigateToCategoryInsertUpdate  = { currentIsExpense->
                    appNavigation.navigateToCategoryInsertUpdateScreen(currentIsExpense)
                },
                navigateToTransactionDisplay = {
                    appNavigation.navigateToTransactionsDisplayScreen()
                },
                categoryViewModel = categoryViewModel,
                transactionViewModel = transactionViewModel
            )
        }
        composable(Screens.CategoryInsertUpdateScreen.name + "/{isExpense}") { backStackEntry->
            val isExpense = backStackEntry.arguments?.getString("isExpense")?.toBooleanStrictOrNull()?:false
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            CategoryInsertUpdateScreen(
                isExpense = isExpense,
                navigateBack = appNavigation.navigateBack,
                categoryViewModel = categoryViewModel,
                navigateToParentCategorySelection = {
                    appNavigation.navigateToCategorySelectionScreen(isExpense, false)
                },
            )
        }
        composable(Screens.CategorySelectionScreen.name + "/{isExpense}/{fromTransaction}") { backStackEntry->
            val isExpense = backStackEntry.arguments?.getString("isExpense")?.toBooleanStrictOrNull()?:false
            val fromTransaction = backStackEntry.arguments?.getString("fromTransaction")?.toBooleanStrictOrNull()?:false
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            CategorySelectionScreen(
                isExpense = isExpense,
                fromTransaction = fromTransaction,
                navigateBack = appNavigation.navigateBack,
                categoryViewModel = categoryViewModel,
                transactionsViewModel = transactionViewModel
            )
        }
        //transaction screens
        composable(Screens.TransactionDisplayScreen.name){
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            val accountsViewModel = hiltViewModel<AccountsViewModel>(parentEntry)
            TransactionsDisplayScreen(
                navigateToTransactionInsertUpdate = {
                    appNavigation.navigateToTransactionInsertScreen()
                },
                navigateToAccountSelection = appNavigation.navigateToAccountsSelectionScreen,
                navigateBack = appNavigation.navigateBack,
                navigateToFilterScreen = {
                    appNavigation.navigateToFilterScreen()
                },
                transactionViewModel = transactionViewModel,
                accountsViewModel = accountsViewModel,
                categoryViewModel = categoryViewModel
            )
        }
        //transaction screens
        composable(Screens.FilterScreen.name){
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            FilterScreen(
                navigateBack = appNavigation.navigateBack,
                transactionsViewModel = transactionViewModel,
                categoryViewModel = categoryViewModel
            )
        }
        composable(Screens.TransactionInsertScreen.name){ backStackEntry->
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            TransactionInsertScreen(
                navigateToCurrencySelection = {
                },
                navigateToCategorySelection = { isExpense->
                    appNavigation.navigateToCategorySelectionScreen(isExpense, true)
                },
                navController = navController,
                navigateToDateTimePicker = { initialDateTime->
                    appNavigation.navigateToDateTimePickerScreen(initialDateTime)
                },
                navigateToAccountSelection ={
                    appNavigation.navigateToAccountsSelectionScreen()
                },
                navigateBack = appNavigation.navigateBack,
                transactionViewModel = transactionViewModel,
                categoryViewModel = categoryViewModel
            )
        }
        composable(Screens.DateTimePickerScreen.name + "/{initialDateTime}"){ backStackEntry->
            val initialDateTime = backStackEntry.arguments?.getString("initialDateTime")?.toLongOrNull()?:System.currentTimeMillis()
            log("DateTimePickerScreen:initialDateTime: ${initialDateTime.toDateString()}")
            DateTimePickerScreen(
                initialDateTime = initialDateTime,
                onDateTimeSelected = { pickedDateTime->
                    log("pickedDateTime:: ${pickedDateTime.toDateString()}")
                    appNavigation.navigateBack()
                    navController.currentBackStackEntry?.savedStateHandle?.set("selectedDateTime", pickedDateTime)
                },
                navigateBack = {appNavigation.navigateBack()}
            )
        }

        //accounts screens
        composable(Screens.AccountsSelectionScreen.name){ backStackEntry->
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val accountsViewModel = hiltViewModel<AccountsViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            AccountSelectionScreen(
                navigateToAccountInsertUpdate = {
                    appNavigation.navigateToAccountsInsertUpdateScreen()
                },
                navigateBack = appNavigation.navigateBack,
                commonAdsUtil = commonAdsUtil,
                accountsViewModel = accountsViewModel,
                categoryViewModel = categoryViewModel,
                transactionsViewModel = transactionViewModel
            )
        }
        composable(Screens.AccountsInsertUpdateScreen.name){
            val parentEntry = remember {
                navController.getBackStackEntry(Screens.MainHomeScreen.name)
            }
            val categoryViewModel = hiltViewModel<CategoryViewModel>(parentEntry)
            val accountsViewModel = hiltViewModel<AccountsViewModel>(parentEntry)
            val transactionViewModel = hiltViewModel<TransactionsViewModel>(parentEntry)
            AccountInsertUpdateScreen(
                navigateToCurrencySelection = {},
                navigateBack = appNavigation.navigateBack,
                accountsViewModel = accountsViewModel,
                categoryViewModel = categoryViewModel,
                transactionsViewModel = transactionViewModel
            )
        }
        
        // Presentation screens
        composable(Screens.PresentationMainScreen.name) {
            com.example.budgetly.ui.presentation.PresentationMainScreen(
                onNavigateToScreen = { screen ->
                    when (screen) {
                        com.example.budgetly.ui.presentation.PresentationScreens.SideEffectsDemo -> {
                            appNavigation.navigateToSideEffectsDemo()
                        }
                        com.example.budgetly.ui.presentation.PresentationScreens.ModifierDemo -> {
                            appNavigation.navigateToModifierDemo()
                        }
                        com.example.budgetly.ui.presentation.PresentationScreens.ListDemo -> {
                            appNavigation.navigateToListDemo()
                        }
                        com.example.budgetly.ui.presentation.PresentationScreens.TextDemo -> {
                            appNavigation.navigateToTextDemo()
                        }
                        com.example.budgetly.ui.presentation.PresentationScreens.ImageDemo -> {
                            appNavigation.navigateToImageDemo()
                        }
                        com.example.budgetly.ui.presentation.PresentationScreens.StateHoistingDemo -> {
                            appNavigation.navigateToStateHoistingDemo()
                        }
                        com.example.budgetly.ui.presentation.PresentationScreens.ThemingDemo -> {
                            appNavigation.navigateToThemingDemo()
                        }
                    }
                }
            )
        }
        composable(Screens.SideEffectsDemo.name) {
            com.example.budgetly.ui.presentation.side_effects.SideEffectsDemoScreen(
                onBackClick = { appNavigation.navigateBack() }
            )
        }
        composable(Screens.ModifierDemo.name) {
            com.example.budgetly.ui.presentation.modifier.ModifierDemoScreen(
                onBackClick = { appNavigation.navigateBack() }
            )
        }
        composable(Screens.ListDemo.name) {
            com.example.budgetly.ui.presentation.list.ListDemoScreen(
                onBackClick = { appNavigation.navigateBack() }
            )
        }
        composable(Screens.TextDemo.name) {
            com.example.budgetly.ui.presentation.text.TextDemoScreen(
                onBackClick = { appNavigation.navigateBack() }
            )
        }
        composable(Screens.ImageDemo.name) {
            com.example.budgetly.ui.presentation.image.ImageDemoScreen(
                onBackClick = { appNavigation.navigateBack() }
            )
        }
        composable(Screens.StateHoistingDemo.name) {
            com.example.budgetly.ui.presentation.state_hoisting.StateHoistingDemoScreen(
                onBackClick = { appNavigation.navigateBack() }
            )
        }
        composable(Screens.ThemingDemo.name) {
            com.example.budgetly.ui.presentation.theming.ThemingDemoScreen(
                onBackClick = { appNavigation.navigateBack() }
            )
        }
    }
}