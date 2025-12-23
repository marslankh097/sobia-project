package com.example.budgetly.data.local.database.dao.local.cash

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgetly.data.local.database.entities.local.cash.SubCategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import com.example.budgetly.data.mappers.local.subcategory.SubCategoryTotal
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(subCategory: SubCategoryEntity): Long

    @Query("SELECT * FROM subcategories WHERE categoryId = :categoryId")
     fun getSubCategoriesByCategoryId(categoryId: Long): Flow<List<SubCategoryEntity>>

    @Query("SELECT * FROM subcategories")
     fun getAllSubCategories(): Flow<List<SubCategoryEntity>>

    @Query("SELECT * FROM subcategories WHERE subCategoryId = :id")
    suspend fun getSubCategoryById(id: Long): SubCategoryEntity?

    @Query("DELETE FROM subcategories WHERE subCategoryId = :id")
    suspend fun deleteSubCategoryById(id: Long)

    @Delete
    suspend fun deleteSubCategory(subCategory: SubCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategories(subCategories: List<SubCategoryEntity>)

    @Update
    suspend fun updateSubCategory(subCategory: SubCategoryEntity)

    @Query("SELECT * FROM subcategories WHERE predefined = 1")
     fun getPredefinedSubCategories(): Flow<List<SubCategoryEntity>>

    @Query(" SELECT * FROM transactions WHERE accountId = :accountId")
    fun getAllTransactionsByAccountId(accountId: Long = 1): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE subcategoryId = :subCategoryId AND accountId = :accountId")
    fun getTransactionsBySubCategoryId(accountId:Long = 1,subCategoryId: Long): Flow<List<TransactionEntity>>

    @Query("""
    SELECT t.subcategoryId, 
           SUM(CAST(t.amount AS REAL)) AS total, 
           a.currency
    FROM transactions t
    JOIN accounts a ON a.accountId = t.accountId
    WHERE t.accountId = :accountId AND t.date BETWEEN :fromTimeStamp AND :toTimeStamp
    GROUP BY t.subcategoryId, a.currency
""")
    fun getTransactionTotalBySubCategory(accountId: Long = 1,fromTimeStamp: Long, toTimeStamp: Long): Flow<List<SubCategoryTotal>>

    @Query("""
    SELECT t.subcategoryId, 
           SUM(CAST(t.amount AS REAL)) AS total, 
           a.currency
    FROM transactions t
    JOIN accounts a ON a.accountId = t.accountId
    WHERE t.accountId = :accountId 
    GROUP BY t.subcategoryId, a.currency
""")
    fun getTransactionTotalBySubCategory(accountId: Long = 1): Flow<List<SubCategoryTotal>>
}

