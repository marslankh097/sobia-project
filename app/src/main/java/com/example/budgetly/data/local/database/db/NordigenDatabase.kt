package com.example.budgetly.data.local.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetly.data.local.database.dao.api.nordigen.AgreementDao
import com.example.budgetly.data.local.database.dao.api.nordigen.RequisitionDao
import com.example.budgetly.data.local.database.entities.api.nordigen.CachedAgreement
import com.example.budgetly.data.local.database.entities.api.nordigen.CachedRequisition

@Database(entities = [
    CachedRequisition::class,
    CachedAgreement::class],
    version = 1,exportSchema = false )
@TypeConverters(Converters::class)
abstract class NordigenDatabase : RoomDatabase() {
    abstract fun requisitionDao(): RequisitionDao
    abstract fun agreementDao(): AgreementDao
}