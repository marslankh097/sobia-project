package com.example.budgetly.data.local.database.dao.api.receipt
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptItemEntity
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptResponseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    // Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceiptResponse(response: ReceiptResponseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceiptItems(items: List<ReceiptItemEntity>)

    // Get
    @Transaction
    @Query("""
    SELECT * FROM receipt_response
    WHERE id IN (
        SELECT receiptResponseId FROM receipt_item
        UNION
        SELECT id FROM receipt_response
    )
""")
    fun getAllReceiptResponses(): Flow<List<ReceiptResponseWithItems>>

    @Transaction
    @Query("SELECT * FROM receipt_response WHERE id = :receiptId")
    suspend fun getReceiptById(receiptId: Long): ReceiptResponseWithItems?

    @Query("SELECT * FROM receipt_item WHERE receiptResponseId = :receiptId")
    fun getItemsForReceipt(receiptId: Long): Flow<List<ReceiptItemEntity>>

    // Delete
    @Query("DELETE FROM receipt_response WHERE id = :receiptId")
    suspend fun deleteReceiptResponseById(receiptId: Long)

    @Query("DELETE FROM receipt_item WHERE id = :itemId")
    suspend fun deleteReceiptItemById(itemId: Long)

    @Query("DELETE FROM receipt_item")
    suspend fun deleteAllReceipts()

    // Update
    @Update
    suspend fun updateReceiptItem(item: ReceiptItemEntity)

    // Update ReceiptResponse
    @Update
    suspend fun updateReceiptResponse(response: ReceiptResponseEntity)

}
