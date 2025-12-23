package com.example.budgetly.data.local.database.entities.api.nordigen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agreements")
data class CachedAgreement(
    @PrimaryKey val agreementId: String,
    val institutionId: String,
    val created: String, // ISO-8601 format
    val accessValidForDays: Int
)
