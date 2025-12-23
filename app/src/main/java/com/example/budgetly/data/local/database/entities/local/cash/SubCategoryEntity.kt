package com.example.budgetly.data.local.database.entities.local.cash

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.budgetly.domain.models.enums.category.CategoryUrgency

@Entity(
    tableName = "subcategories",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("categoryId")]
)
data class SubCategoryEntity(
    val categoryId: Long,
    @PrimaryKey(autoGenerate = true) val subCategoryId: Long = 0L,
    val subCategoryName: String,
    val urgency: String = CategoryUrgency.Need.name,
    val predefined:Boolean = false,
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)