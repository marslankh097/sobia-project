package com.example.budgetly.data.local.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetly.data.local.database.dao.api.assistant.AssistantResponseDao
import com.example.budgetly.data.local.database.dao.api.assistant.ChatDao
import com.example.budgetly.data.local.database.entities.api.assistant.AssistantResponseEntity
import com.example.budgetly.data.local.database.entities.api.assistant.ChatEntity

@Database(entities = [
    ChatEntity::class,
    AssistantResponseEntity::class],
    version = 1,exportSchema = false )
@TypeConverters(Converters::class)
abstract class AssistantDatabase : RoomDatabase() {
    abstract fun assistantResponseDao(): AssistantResponseDao
    abstract fun chatDao(): ChatDao
}