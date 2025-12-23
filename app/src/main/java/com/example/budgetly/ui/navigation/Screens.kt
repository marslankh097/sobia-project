package com.example.budgetly.ui.navigation
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

enum class Screens {

    SplashScreen,
    OnBoardingScreen,
    MainHomeScreen,

    //Notification Listener
    NotificationScreen,
    NotificationSettingScreen,


    //receipt screens
    CameraGalleryScreen,
    ReceiptProcessingScreen,
    ReceiptHistoryScreen,
    ReceiptResultScreen,

    //Assistant Screens
    AssistantScreen,
    AssistantHistoryScreen,
    PineConeScreen,

    //Accounts Screens
    AccountsSelectionScreen,
    AccountsInsertUpdateScreen,


    //category Screens
    CategoryDisplayScreen,
    PieChartDetailScreen,
    CategoryInsertUpdateScreen,
    CategorySelectionScreen,

//    transaction screens
    FilterScreen,
    TransactionDisplayScreen,
    TransactionInsertScreen,
    DateTimePickerScreen,

    //banking api screens
    InstitutionScreen,
    RequisitionScreen,
    AccountsScreen,
    TransactionScreen,
    
    //Presentation screens
    PresentationMainScreen,
    SideEffectsDemo,
    ModifierDemo,
    ListDemo,
    TextDemo,
    ImageDemo,
    StateHoistingDemo,
    ThemingDemo
}
val LocalNavHostController = compositionLocalOf<NavHostController> {
    error("")
}
