package com.example.budgetly.data.local.database.dao.api.assistant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetly.data.local.database.entities.api.assistant.AssistantResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssistantResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: AssistantResponseEntity)

    @Query("SELECT * FROM assistant_responses WHERE id = :responseId LIMIT 1")
    suspend fun getResponseById(responseId: String): AssistantResponseEntity?

    @Query("DELETE FROM assistant_responses WHERE id = :responseId")
    suspend fun deleteResponseById(responseId: String)

    @Query("DELETE FROM assistant_responses WHERE chatOwnerId = :chatId")
    suspend fun deleteResponsesByChatId(chatId: String)

    @Query("SELECT * FROM assistant_responses WHERE chatOwnerId = :chatId ORDER BY timestamp ASC")
    fun getAllResponsesByChatId(chatId: String): Flow<List<AssistantResponseEntity>>

    @Query("""
        SELECT * FROM assistant_responses 
        WHERE chatOwnerId = :chatId AND 
              (originalQuestion LIKE '%' || :query || '%' 
              OR naturalLanguageResponse LIKE '%' || :query || '%')
        ORDER BY timestamp ASC
    """)
    fun searchResponsesInChat(chatId: String, query: String): Flow<List<AssistantResponseEntity>>
}
