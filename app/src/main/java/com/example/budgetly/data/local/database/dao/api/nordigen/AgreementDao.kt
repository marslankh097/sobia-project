package com.example.budgetly.data.local.database.dao.api.nordigen

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetly.data.local.database.entities.api.nordigen.CachedAgreement

@Dao
interface AgreementDao {
    @Query("SELECT * FROM agreements WHERE institutionId = :institutionId LIMIT 1")
    suspend fun getAgreementForInstitution(institutionId: String): CachedAgreement?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgreement(agreement: CachedAgreement)

    @Query("DELETE FROM agreements WHERE institutionId = :institutionId")
    suspend fun deleteAgreement(institutionId: String)
}
