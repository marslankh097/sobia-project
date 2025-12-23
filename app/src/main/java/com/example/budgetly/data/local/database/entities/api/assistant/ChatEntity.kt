package com.example.budgetly.data.local.database.entities.api.assistant

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val chatId: String = UUID.randomUUID().toString(),
    val date: Long = System.currentTimeMillis(),
    val title: String = "Untitled Chat",
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis()
)
