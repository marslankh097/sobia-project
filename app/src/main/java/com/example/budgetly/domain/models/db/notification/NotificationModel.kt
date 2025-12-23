package com.example.budgetly.domain.models.db.notification

data class NotificationModel(
    val id: Long = 0L,
    val title: String,
    val message: String,
    val pkgName: String,
    val appName: String,
    val timeInMillis: Long = System.currentTimeMillis()
)