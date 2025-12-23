package com.example.budgetly.data.local.datasources.db.local.notification

import com.example.budgetly.data.local.database.entities.local.notification.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationDataSource {
    suspend fun insert(notification: NotificationEntity)
    fun getAllNotifications(): Flow<List<NotificationEntity>>
    suspend fun deleteAll()
    suspend fun deleteById(id: Long)
    suspend fun getById(id: Long): NotificationEntity?
}
