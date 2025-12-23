package com.example.budgetly.data.local.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetly.data.local.database.dao.api.receipt.ReceiptDao
import com.example.budgetly.data.local.database.dao.local.cash.AccountDao
import com.example.budgetly.data.local.database.dao.local.cash.CategoryDao
import com.example.budgetly.data.local.database.dao.local.cash.SubCategoryDao
import com.example.budgetly.data.local.database.dao.local.cash.TransactionDao
import com.example.budgetly.data.local.database.dao.local.notification.NotificationDao
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptItemEntity
import com.example.budgetly.data.local.database.entities.api.receipt.ReceiptResponseEntity
import com.example.budgetly.data.local.database.entities.local.cash.AccountEntity
import com.example.budgetly.data.local.database.entities.local.cash.CategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.SubCategoryEntity
import com.example.budgetly.data.local.database.entities.local.cash.TransactionEntity
import com.example.budgetly.data.local.database.entities.local.notification.NotificationEntity

@Database(entities = [
    TransactionEntity::class,
    NotificationEntity::class,
    AccountEntity::class,
    ReceiptResponseEntity::class,
    ReceiptItemEntity::class,
    SubCategoryEntity::class,
    CategoryEntity::class],
    version = 1,exportSchema = false )
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun receiptDao(): ReceiptDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun subcategoryDao(): SubCategoryDao
    abstract fun  accountDao(): AccountDao
    abstract fun  notificationDao(): NotificationDao
}