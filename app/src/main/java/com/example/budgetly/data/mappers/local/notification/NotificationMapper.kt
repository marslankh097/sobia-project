package com.example.budgetly.data.mappers.local.notification

import com.example.budgetly.data.local.database.entities.local.notification.NotificationEntity
import com.example.budgetly.domain.models.db.notification.NotificationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Single Entity → Model
fun NotificationEntity.toNotificationModel(): NotificationModel {
    return NotificationModel(
        id = this.id,
        title = this.title ?: "",
        message = this.text ?: "",
        pkgName = this.packageName,
        appName = this.appName,
        timeInMillis = this.timestamp,

    )
}

// Single Model → Entity
fun NotificationModel.toNotificationEntity(): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        title = this.title,
        text = this.message,
        packageName = this.pkgName,
        appName = this.appName,
        timestamp = this.timeInMillis
    )
}

// List<Entity> → List<Model>
fun List<NotificationEntity>.toNotificationModelList(): List<NotificationModel> =
    this.map { it.toNotificationModel() }

// List<Model> → List<Entity>
fun List<NotificationModel>.toNotificationEntityList(): List<NotificationEntity> =
    this.map { it.toNotificationEntity() }

// Flow<List<Entity>> → Flow<List<Model>>
fun Flow<List<NotificationEntity>>.toNotificationModelFlow(): Flow<List<NotificationModel>> =
    this.map { it.toNotificationModelList() }

// Flow<List<Model>> → Flow<List<Entity>>
fun Flow<List<NotificationModel>>.toNotificationEntityFlow(): Flow<List<NotificationEntity>> =
    this.map { it.toNotificationEntityList() }

// Optional: Resolve app name from package name
private fun resolveAppName(packageName: String): String {
    return when (packageName) {
        "com.google.android.gm" -> "Gmail"
        "com.google.android.apps.messaging",
        "com.samsung.android.messaging",
        "com.android.mms" -> "SMS"
        else -> packageName
    }
}
