package com.example.budgetly.data.repositories_impl.db_repo_impl.notification

import com.example.budgetly.data.local.datasources.db.local.notification.NotificationDataSource
import com.example.budgetly.data.mappers.local.notification.toNotificationEntity
import com.example.budgetly.data.mappers.local.notification.toNotificationModel
import com.example.budgetly.data.mappers.local.notification.toNotificationModelFlow
import com.example.budgetly.domain.models.db.notification.NotificationModel
import com.example.budgetly.domain.repositories.db.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor(
    private val dataSource: NotificationDataSource
) : NotificationRepository {

    override suspend fun saveNotification(notification: NotificationModel) {
        dataSource.insert(notification.toNotificationEntity())
    }

    override fun getAllNotifications(): Flow<List<NotificationModel>> {
        return dataSource.getAllNotifications().toNotificationModelFlow()
    }

    override suspend fun deleteNotificationById(id: Long) {
        dataSource.deleteById(id)
    }

    override suspend fun deleteAllNotifications() {
        dataSource.deleteAll()
    }

    override suspend fun getNotificationById(id: Long): NotificationModel? {
        return dataSource.getById(id)?.toNotificationModel()
    }
}
