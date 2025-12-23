package com.example.budgetly.data.local.database.entities.api.nordigen

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requisitions")
data class CachedRequisition(
    @PrimaryKey val requisitionId: String,
     val created: String? = null,
     val redirect: String?,
     val status: String,
     val institutionId: String,
     val agreement: String,
     val reference: String? = null,
     val accounts: List<String>? = null,
     val userLanguage: String? = null,
     val link: String,
     val ssn: String? = null,
     val accountSelection: Boolean = false,
     val redirectImmediate: Boolean = false
)