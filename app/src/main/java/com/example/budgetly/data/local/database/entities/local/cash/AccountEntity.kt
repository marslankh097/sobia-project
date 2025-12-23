package com.example.budgetly.data.local.database.entities.local.cash
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetly.domain.models.enums.account.AccountCategory
import com.example.budgetly.domain.models.enums.account.AccountType

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val accountId: Long = 0L,
    val accountIBAN: String,
    val accountTyp: String = AccountType.Cash.name,
    val accountCategory: String = AccountCategory.Personal.name,
    val balance: String,
    var initialBalance:String,
    val currency: String,
    val createdAt:Long = System.currentTimeMillis(),
    val lastModified:Long = System.currentTimeMillis(),
)
