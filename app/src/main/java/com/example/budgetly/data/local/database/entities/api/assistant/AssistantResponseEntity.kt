package com.example.budgetly.data.local.database.entities.api.assistant

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "assistant_responses",
    foreignKeys = [ForeignKey(
        entity = ChatEntity::class,
        parentColumns = ["chatId"],
        childColumns = ["chatOwnerId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("chatOwnerId")]
)
data class AssistantResponseEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val chatOwnerId: String,
    val originalQuestion: String,
    val generatedSql: String,
    val naturalLanguageResponse: String,
    val rawResults: String, // JSON or comma-separated
    val error: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)
