package com.example.budgetly.ui.notification_listener.screen

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.BannerAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.notification_listener.NotificationViewModel
import com.example.budgetly.ui.notification_listener.events.NotificationEvent
import com.example.budgetly.ui.notification_listener.screen.content.NotificationRow
import com.example.budgetly.ui.notification_listener.screen.content.PermissionComponent
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.permissions.PermissionStatus
import com.example.budgetly.utils.permissions.isNotificationServiceEnabled
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toDateString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    navigateToNotificationSetting:()->Unit,
    navigateBack: () -> Unit
) {
    val notificationState by notificationViewModel.notificationState.collectAsState()
    val expandedNotifications by notificationViewModel.expandedNotifications.collectAsState()
    val context = LocalContext.current
    val handleBack = {
        navigateBack()
    }
    BackHandler { handleBack() }
    LaunchedEffect(Unit) {
        val permissionStatus = if (context.isNotificationServiceEnabled()) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.DENIED
        }
        notificationViewModel.onEvent(NotificationEvent.SetPermissionStatus(permissionStatus))
    }
    val notificationListenerPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (context.isNotificationServiceEnabled()) {
            notificationViewModel.onEvent(NotificationEvent.SetPermissionStatus(PermissionStatus.GRANTED))
        }
    }
    if (notificationState.showDeleteNotificationDialog || notificationState.showDeleteAllDialog) {
        SimpleAlertDialog(
            title = if (notificationState.showDeleteNotificationDialog) {
                stringResource(R.string.discard_notification)
            } else {
                stringResource(R.string.discard_all_notifications)
            },
            msg = if (notificationState.showDeleteNotificationDialog) {
                stringResource(R.string.are_you_sure_you_want_to_discard_this_notification)
            } else {
                stringResource(R.string.are_you_sure_you_want_to_discard_all_notifications_from_your_history)
            },
            positiveText = stringResource(R.string.discard),
            negativeText = stringResource(R.string.cancel)
        ) {
            if (it) {
                if (notificationState.showDeleteNotificationDialog) {
                    notificationViewModel.onEvent(NotificationEvent.DeleteNotification)
                } else {
                    notificationViewModel.onEvent(NotificationEvent.DeleteAllNotifications)
                }
            }
            if (notificationState.showDeleteNotificationDialog) {
                notificationViewModel.onEvent(NotificationEvent.ShowDeleteNotificationDialog(false))
            } else {
                notificationViewModel.onEvent(NotificationEvent.ShowDeleteAllDialog(false))
            }
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(when(notificationState.notificationPermissionStatus){
                PermissionStatus.GRANTED -> R.string.notification_listener
                PermissionStatus.DENIED -> R.string.enable_notifications
            }
            ),
            icon2 = if(notificationState.notificationPermissionStatus == PermissionStatus.GRANTED) R.drawable.icon_notification_settings else null,
            onClickIcon2 = {
                //navigate to notification settings
                navigateToNotificationSetting()
            },
            icon3 = if (notificationState.notificationPermissionStatus == PermissionStatus.GRANTED && notificationState.notifications.isNotEmpty()) R.drawable.img_delete else null,
            onClickIcon3 =
                if (notificationState.notifications.isNotEmpty()) {
                    {
                        //delete all chats click
                        notificationViewModel.onEvent(NotificationEvent.ShowDeleteAllDialog(true))
                    }
                } else null
        ) {
            handleBack()
        }
        Column(modifier = modifier.fillMaxWidth().weight(1f)){
            when (notificationState.notificationPermissionStatus) {
                PermissionStatus.GRANTED -> {
                    when {
                        notificationState.isLoading -> {
                            //when permission granted and loading
                            LoadingComponent(
                                textId = R.string.fetching_notifications
                            )
                        }

                        else -> {
                            if (notificationState.notifications.isEmpty()) {
                                //when no notification captured yet
                                EmptyComponent(
                                    modifier = Modifier.fillMaxSize(),
                                    image = R.drawable.icon_notification,
                                    text = R.string.no_notifications_available_yet
                                )
                            } else {
                                //        if there are some notifications
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .background(color = secondaryBgColor)
                                        .padding(horizontal = 12.sdp, vertical = 12.sdp),
                                    contentPadding = PaddingValues(bottom = 12.sdp),
                                    verticalArrangement = Arrangement.spacedBy(10.sdp)
                                ) {
                                    items(notificationState.notifications) { notification ->
                                        val icon: Drawable? = try {
                                            context.packageManager.getApplicationIcon(notification.pkgName)
                                        } catch (e: Exception) {
                                            null
                                        }
                                        val placeholder = when (notification.appName) {
                                            "Gmail" ->  R.drawable.img_email_notification
                                            "SMS" ->  R.drawable.img_sms_notification
                                            "WhatsApp" -> R.drawable.img_whatsapp_notification
                                            else -> R.drawable.icon_notification
                                        }
                                        NotificationRow(
                                            icon = icon,
                                            placeholder = placeholder,
                                            modifier = Modifier.fillMaxWidth(),
                                            appName = notification.appName,
                                            title = notification.title,
                                            msg = notification.message,
                                            subTitle = notification.timeInMillis.toDateString("dd_MMM_yyyy"),
                                            isExpanded = notification.id in expandedNotifications,
                                            onNotificationSelected = {
                                                notificationViewModel.onEvent(NotificationEvent.ToggleNotification(notification.id))
                                            },
                                            onButtonClick = {
                                                // parse notification content and add as a transaction

                                            },
                                            onDeleteClicked = {
                                                notificationViewModel.onEvent(
                                                    NotificationEvent.SetTempNotification(
                                                        notification.id
                                                    )
                                                )
                                                notificationViewModel.onEvent(
                                                    NotificationEvent.ShowDeleteNotificationDialog(
                                                        true
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                PermissionStatus.DENIED -> {
                    //when(no notification listener permission allowed yet )
                    PermissionComponent(
                        modifier = Modifier.fillMaxSize(),
                        context = context,
                        icon = R.raw.gif_notification_permission,
                        title = stringResource(R.string.enable_notification_permission_to_listen_and_capture_your_financial_notifications),
                        msg = stringResource(R.string.please_find_app_on_the_next_screen_and_enable_this_feature),
                        showSwitchRow = true,
                        onButtonClick = {
                            //click on enable button
                            notificationListenerPermissionLauncher.launch(
                                Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                            )
                        }
                    )
                }
            }
        }
        VerticalSpacer()
        BannerAd(
            context = context as Activity,
            adKey = AdKeys.NotificationBanner.name,
            placementKey = RemoteConfigKeys.NOTIFICATION_BANNER_ENABLED,
            screenName = "Notification_Screen_Bottom"
        )
        VerticalSpacer(5)

    }
}