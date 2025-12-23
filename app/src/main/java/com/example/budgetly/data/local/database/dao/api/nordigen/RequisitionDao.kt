package com.example.budgetly.data.local.database.dao.api.nordigen

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetly.data.local.database.entities.api.nordigen.CachedRequisition

@Dao
interface RequisitionDao {
    @Query("SELECT * FROM requisitions WHERE institutionId = :institutionId AND agreement = :agreementId LIMIT 1")
    suspend fun getRequisition(institutionId: String,agreementId:String): CachedRequisition?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequisition(requisition: CachedRequisition)

    @Query("DELETE FROM requisitions WHERE institutionId = :institutionId")
    suspend fun deleteRequisition(institutionId: String)
}
