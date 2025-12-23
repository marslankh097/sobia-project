 package com.example.budgetly.ui.navigation
import android.util.Log
import androidx.navigation.NavHostController
import com.example.budgetly.utils.firstEntry

 private const val TAG = "AppNavigationActionsTAG"

class AppNavigationActions(
    private val navHostController: NavHostController,
) {
    val navigateToSplashScreen: () -> Unit = {
        navHostController.navigate(Screens.SplashScreen .name)
    }
    val navigateToInstitutionScreen: () -> Unit = {
        navHostController.navigate(Screens.InstitutionScreen.name)
    }
    val navigateToOnBoardingScreen: () -> Unit = {
        navigateClearingAll(Screens.OnBoardingScreen.name)
    }
    val navigateToAssistantScreen: () -> Unit = {
        navHostController.navigate(Screens.AssistantScreen.name)
    }
    val navigateToAssistantHistoryScreen: () -> Unit = {
        navHostController.navigate(Screens.AssistantHistoryScreen.name)
    }
    val navigateToPineConeScreen: () -> Unit = {
        navHostController.navigate(Screens.PineConeScreen.name)
    }
    val navigateToNotificationScreen: () -> Unit = {
        navHostController.navigate(Screens.NotificationScreen.name)
    }
    val navigateToNotificationSettingsScreen: () -> Unit = {
        navHostController.navigate(Screens.NotificationSettingScreen.name)
    }
    val navigateToCameraGallery: () -> Unit = {
        navHostController.navigate(Screens.CameraGalleryScreen.name)
    }
    val navigateToReceiptProcessing: () -> Unit = {
        navHostController.navigate(Screens.ReceiptProcessingScreen.name)
    }
    val navigateToReceiptHistory: () -> Unit = {
        navHostController.navigate(Screens.ReceiptHistoryScreen.name)
    }
    val navigateToReceiptResult: () -> Unit = {
        navHostController.navigate(Screens.ReceiptResultScreen.name)
    }
    val navigateToAccountsInsertUpdateScreen: () -> Unit = {
        navHostController.navigate(Screens.AccountsInsertUpdateScreen.name)
    }
    val navigateToAccountsSelectionScreen: () -> Unit = {
        navHostController.navigate(Screens.AccountsSelectionScreen.name)
    }
    val navigateToRequisitionScreen: (String) -> Unit = {
        navHostController.navigate(Screens.RequisitionScreen.name.createRoute(listOf(it)))
    }
    val navigateToTransactionScreen: (String) -> Unit = {
        navHostController.navigate(Screens.TransactionScreen.name.createRoute(listOf(it)))
    }
    val navigateToAccountsScreen: (String) -> Unit = {
        navHostController.popBackStack()
        navHostController.navigate(Screens.AccountsScreen.name.createRoute(listOf(it)))
    }
    val navigateToMainHomeScreen: () -> Unit = {
        navHostController.navigate(Screens.MainHomeScreen.name)
    }
    val navigateToCategoryDisplayScreen: (Boolean) -> Unit = {
        navHostController.navigate(Screens.CategoryDisplayScreen.name.createRoute(listOf(it)))
    }
    val navigateToPieChartDetailScreen: (Boolean, Boolean) -> Unit = {isExpense, isSubCategoryView->
        navHostController.navigate(Screens.PieChartDetailScreen.name.createRoute(listOf(isExpense, isSubCategoryView)))
    }
    val navigateToTransactionsDisplayScreen: () -> Unit = {
        navHostController.navigate(Screens.TransactionDisplayScreen.name)
    }
    val navigateToFilterScreen: () -> Unit = {
        navHostController.navigate(Screens.FilterScreen.name)
    }
    val navigateToTransactionInsertScreen: () -> Unit = {
        navHostController.navigate(Screens.TransactionInsertScreen.name)
    }
    val navigateToCategoryInsertUpdateScreen: (Boolean) -> Unit = { isExpense->
        navHostController.navigate(Screens.CategoryInsertUpdateScreen.name.createRoute(listOf(isExpense)))
    }
    val navigateToCategorySelectionScreen: (Boolean,Boolean) -> Unit = { isExpense, fromTransaction->
        navHostController.navigate(Screens.CategorySelectionScreen.name.createRoute(listOf(isExpense, fromTransaction)))
    }
    val navigateToDateTimePickerScreen: (Long) -> Unit = { initialDateTime->
        navHostController.navigate(Screens.DateTimePickerScreen.name.createRoute(listOf(initialDateTime)))
    }
    val navigateToPresentationMainScreen: () -> Unit = {
        navHostController.navigate(Screens.PresentationMainScreen.name)
    }
    val navigateToSideEffectsDemo: () -> Unit = {
        navHostController.navigate(Screens.SideEffectsDemo.name)
    }
    val navigateToModifierDemo: () -> Unit = {
        navHostController.navigate(Screens.ModifierDemo.name)
    }
    val navigateToListDemo: () -> Unit = {
        navHostController.navigate(Screens.ListDemo.name)
    }
    val navigateToTextDemo: () -> Unit = {
        navHostController.navigate(Screens.TextDemo.name)
    }
    val navigateToImageDemo: () -> Unit = {
        navHostController.navigate(Screens.ImageDemo.name)
    }
    val navigateToStateHoistingDemo: () -> Unit = {
        navHostController.navigate(Screens.StateHoistingDemo.name)
    }
    val navigateToThemingDemo: () -> Unit = {
        navHostController.navigate(Screens.ThemingDemo.name)
    }
    val navigateBack: () -> Unit = {
        if (navHostController.previousBackStackEntry != null) {
            navHostController.popBackStack()
        }
    }
    private fun navigateClearingAll(route: String) {
        navHostController.navigate(route) {
            navHostController.visibleEntries.value.firstEntry()?.destination?.route?.let {
                Log.d(TAG, "navigateClearingAll: popping up to $it while navigating to $route")
                popUpTo(it) {
                    inclusive = true
                }
            }
        }
    }
    private fun String.createRoute(params: List<Any?>): String {
        return buildString {
            append(this@createRoute)
            params.forEach { param ->
                append("/$param")
            }
        }
    }
}