package com.example.budgetly.data.local.database.entities.local.cash
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency
import com.example.budgetly.domain.models.enums.transaction.TransactionType

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(entity = AccountEntity::class, parentColumns = ["accountId"], childColumns = ["accountId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["categoryId"], childColumns = ["categoryId"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = SubCategoryEntity::class, parentColumns = ["subCategoryId"], childColumns = ["subcategoryId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index("accountId"), Index("subcategoryId"), Index("categoryId")]
)
data class TransactionEntity(
    val accountId: Long?,
    val subcategoryId: Long?,
    val categoryId: Long?,
    @PrimaryKey(autoGenerate = true) val transactionId: Long,
    val date: Long= System.currentTimeMillis(),
    val amount: String,
    val type: String = TransactionType.Expense.name,
    val frequency: String = TransactionFrequency.OneTime.name,
    val currency: String,
    val description: String?,
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)