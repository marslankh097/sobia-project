package com.example.budgetly.data.local.database.entities.local.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String?,
    val text: String?,
    val packageName: String,
    val appName: String,
    val timestamp: Long
)
