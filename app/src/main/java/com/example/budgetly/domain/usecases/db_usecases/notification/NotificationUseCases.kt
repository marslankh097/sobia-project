package com.example.budgetly.domain.usecases.db_usecases.notification

import com.example.budgetly.domain.models.db.notification.NotificationModel
import com.example.budgetly.domain.repositories.db.notification.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class NotificationUseCases @Inject constructor(
    val saveNotification: SaveNotificationUseCase,
    val getAllNotifications: GetAllNotificationsUseCase,
    val getNotificationById: GetNotificationByIdUseCase,
    val deleteNotificationById: DeleteNotificationByIdUseCase,
    val deleteAllNotifications: DeleteAllNotificationsUseCase
)
@Singleton
class GetNotificationByIdUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long): NotificationModel? {
        return repository.getNotificationById(id)
    }
}
@Singleton
class DeleteAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllNotifications()
    }
}
@Singleton
class DeleteNotificationByIdUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteNotificationById(id)
    }
}
@Singleton
class GetAllNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<NotificationModel>> {
        return repository.getAllNotifications()
    }
}
@Singleton
class SaveNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: NotificationModel) {
        repository.saveNotification(notification)
    }
}
