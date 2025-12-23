package com.example.budgetly.data.local.database.dao.local.cash

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactions: List<TransactionEntity>)

    @Query("SELECT * FROM transactions")
     fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transactionId = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Query("DELETE FROM transactions WHERE transactionId = :id")
    suspend fun deleteTransactionById(id: Long)




    @Query(" SELECT * FROM transactions WHERE accountId = :accountId AND date BETWEEN :fromTimestamp AND :toTimestamp")
    fun getTransactionsByAccountId(accountId: Long, fromTimestamp: Long, toTimestamp: Long): Flow<List<TransactionEntity>>

    @Query(" SELECT * FROM transactions WHERE accountId = :accountId")
    fun getAllTransactionsByAccountId(accountId: Long = 1): Flow<List<TransactionEntity>>


    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
