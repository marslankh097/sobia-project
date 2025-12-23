package com.example.budgetly.data.local.database.dao.api.assistant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgetly.data.local.database.entities.api.assistant.ChatEntity
import com.example.budgetly.data.mappers.api.assistant.ChatWithCount
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Update
    suspend fun updateChat(chat: ChatEntity)

    @Query("SELECT * FROM chats WHERE chatId = :chatId LIMIT 1")
    suspend fun getChatById(chatId: String): ChatEntity?

    @Query("SELECT * FROM chats ORDER BY date DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("""
    SELECT 
        c.chatId, 
        c.date, 
        c.title, 
        COUNT(r.id) AS chatCount
    FROM chats AS c
    LEFT JOIN assistant_responses AS r ON c.chatId = r.chatOwnerId
    GROUP BY c.chatId
    ORDER BY c.date DESC
""")
    fun getChatsWithCount(): Flow<List<ChatWithCount>>


    @Query("DELETE FROM chats WHERE chatId = :chatId")
    suspend fun deleteChatById(chatId: String)

    @Query("DELETE FROM chats")
    suspend fun deleteAllChats()
}
