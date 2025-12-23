package com.example.budgetly.ui.notification_listener.events

import com.example.budgetly.utils.permissions.PermissionStatus

sealed class NotificationEvent {
    object LoadNotifications : NotificationEvent()
    data object DeleteNotification : NotificationEvent()
    data class ToggleNotification(val notificationId: Long) : NotificationEvent()
    object DeleteAllNotifications : NotificationEvent()
    data class ShowDeleteAllDialog(val show:Boolean) : NotificationEvent()
    data class SetPermissionStatus(val permissionStatus: PermissionStatus) : NotificationEvent()
    data class ShowDeleteNotificationDialog(val show:Boolean) : NotificationEvent()
    data class SetTempNotification(val id:Long) : NotificationEvent()
}
