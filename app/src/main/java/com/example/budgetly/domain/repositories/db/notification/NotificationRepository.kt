package com.example.budgetly.domain.repositories.db.notification

import com.example.budgetly.domain.models.db.notification.NotificationModel
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun saveNotification(notification: NotificationModel)
    fun getAllNotifications(): Flow<List<NotificationModel>>
    suspend fun deleteNotificationById(id: Long)
    suspend fun deleteAllNotifications()
    suspend fun getNotificationById(id: Long): NotificationModel?
}
