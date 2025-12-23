package com.example.budgetly.data.local.database.dao.local.cash

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgetly.data.local.database.entities.local.cash.CategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import com.example.budgetly.data.mappers.local.category.CategoryTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Query("SELECT * FROM categories")
     fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Query("DELETE FROM categories WHERE categoryId = :id")
    suspend fun deleteCategoryById(id: Long)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories WHERE predefined = 1")
     fun getPredefinedCategories(): Flow<List<CategoryEntity>>

    @Query(" SELECT * FROM transactions WHERE accountId = :accountId")
    fun getAllTransactionsByAccountId(accountId: Long = 1): Flow<List<TransactionEntity>>


    @Query("SELECT * FROM transactions WHERE categoryId = :categoryId AND accountId = :accountId")
    fun getTransactionsByCategoryId(accountId:Long = 1, categoryId: Long): Flow<List<TransactionEntity>>

    @Query("""
    SELECT t.categoryId, 
           SUM(CAST(t.amount AS REAL)) AS total, 
           a.currency
    FROM transactions t
    JOIN accounts a ON a.accountId = t.accountId
    WHERE t.accountId = :accountId AND t.date BETWEEN :fromTimeStamp AND :toTimeStamp
    GROUP BY t.categoryId, a.currency
""")
    fun getTransactionTotalByCategory(accountId: Long = 1, fromTimeStamp: Long, toTimeStamp: Long): Flow<List<CategoryTotal>>
    @Query("""
    SELECT t.categoryId, 
           SUM(CAST(t.amount AS REAL)) AS total, 
           a.currency
    FROM transactions t
    JOIN accounts a ON a.accountId = t.accountId
    WHERE t.accountId = :accountId
    GROUP BY t.categoryId, a.currency
""")
    fun getTransactionTotalByCategory(accountId: Long = 1): Flow<List<CategoryTotal>>
}