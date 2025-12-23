package com.example.budgetly.data.local.database.entities.local.cash

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetly.domain.models.enums.category.CategoryType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0L,
    val categoryName: String,
    val categoryType: String = CategoryType.Expense.name,
    val predefined:Boolean = false,
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)