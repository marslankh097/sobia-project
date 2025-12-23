package com.example.budgetly.di.local

import android.content.Context
import androidx.room.Room
import com.example.budgetly.data.local.database.dao.api.assistant.AssistantResponseDao
import com.example.budgetly.data.local.database.dao.api.assistant.ChatDao
import com.example.budgetly.data.local.database.dao.api.nordigen.AgreementDao
import com.example.budgetly.data.local.database.dao.api.nordigen.RequisitionDao
import com.example.budgetly.data.local.database.dao.api.receipt.ReceiptDao
import com.example.budgetly.data.local.database.dao.local.cash.AccountDao
import com.example.budgetly.data.local.database.dao.local.cash.CategoryDao
import com.example.budgetly.data.local.database.dao.local.cash.SubCategoryDao
import com.example.budgetly.data.local.database.dao.local.cash.TransactionDao
import com.example.budgetly.data.local.database.dao.local.notification.NotificationDao
import com.example.budgetly.data.local.database.db.AppDatabase
import com.example.budgetly.data.local.database.db.AssistantDatabase
import com.example.budgetly.data.local.database.db.NordigenDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
 class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).fallbackToDestructiveMigration(false).build()
    }
    @Provides
    @Singleton
    fun provideAssistantDatabase(@ApplicationContext context: Context): AssistantDatabase {
        return Room.databaseBuilder(
                context,
                AssistantDatabase::class.java,
                "assistant_database"
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideNordigenDatabase(@ApplicationContext context: Context): NordigenDatabase {
        return Room.databaseBuilder(
                context,
                NordigenDatabase::class.java,
                "banking_database"
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()

    @Provides
    @Singleton
    fun provideReceiptDao(db: AppDatabase): ReceiptDao = db.receiptDao()
    @Provides
    @Singleton
    fun provideNotificationDao(db: AppDatabase): NotificationDao = db.notificationDao()
    @Provides
    @Singleton
    fun provideChatDao(db: AssistantDatabase): ChatDao = db.chatDao()
    @Provides
    @Singleton
    fun provideAssistantResponseDao(db: AssistantDatabase): AssistantResponseDao = db.assistantResponseDao()
    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()
    @Provides
    @Singleton
    fun provideSubCategoryDao(db: AppDatabase): SubCategoryDao = db.subcategoryDao()
    @Provides
    @Singleton
    fun provideAccountDao(db: AppDatabase): AccountDao = db.accountDao()

    @Provides
    @Singleton
    fun provideRequisitionDao(db: NordigenDatabase): RequisitionDao = db.requisitionDao()

    @Provides
    @Singleton
    fun provideAgreementDao(db: NordigenDatabase): AgreementDao = db.agreementDao()
}
