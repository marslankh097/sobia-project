package com.example.budgetly.data.local.datasources.db.local.notification

import com.example.budgetly.data.local.database.dao.local.notification.NotificationDao
import com.example.budgetly.data.local.database.entities.local.notification.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationDataSourceImpl @Inject constructor(
    private val dao: NotificationDao
) : NotificationDataSource {

    override suspend fun insert(notification: NotificationEntity) {
        dao.insert(notification)
    }

    override fun getAllNotifications(): Flow<List<NotificationEntity>> {
        return dao.getAllNotifications()
    }

    override suspend fun deleteAll() {
        dao.deleteAllNotifications()
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteNotificationById(id)
    }

    override suspend fun getById(id: Long): NotificationEntity? {
        return dao.getNotificationById(id)
    }
}
