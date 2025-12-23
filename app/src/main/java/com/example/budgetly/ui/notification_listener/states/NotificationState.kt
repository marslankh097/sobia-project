package com.example.budgetly.ui.notification_listener.states

import com.example.budgetly.domain.models.db.notification.NotificationModel
import com.example.budgetly.utils.permissions.PermissionStatus

data class NotificationState(
    val notifications: List<NotificationModel> = emptyList(),
    val isLoading: Boolean = false,
    val showDeleteAllDialog:Boolean = false,
    val notificationPermissionStatus: PermissionStatus = PermissionStatus.DENIED,
    val showDeleteNotificationDialog:Boolean = false,
    val tempNotificationId:Long?= null
)
