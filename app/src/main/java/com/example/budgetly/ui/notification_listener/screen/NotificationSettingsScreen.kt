package com.example.budgetly.ui.notification_listener.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetly.ui.notification_listener.NotificationViewModel
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.TopBar
import ir.kaaveh.sdpcompose.sdp

@Composable
fun NotificationSettingsScreen(modifier: Modifier = Modifier, navigateBack:()->Unit,
                               notificationViewModel: NotificationViewModel = hiltViewModel()){
    val context = LocalContext.current
    val handleBack = {
        navigateBack()
    }
    BackHandler { handleBack() }
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = "Notification Settings"
        ){
            handleBack()
        }
        Column(modifier = Modifier
            .weight(1f)
//            .verticalScroll(rememberScrollState(), true)
            .background(secondaryBgColor)
            .padding(12.sdp), verticalArrangement = Arrangement.spacedBy(20.sdp)) {

        }
    }
}