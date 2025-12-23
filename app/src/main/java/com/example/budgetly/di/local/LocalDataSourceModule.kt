package com.example.budgetly.di.local
import com.example.budgetly.data.local.datasources.data_store.PreferenceDataStore
import com.example.budgetly.data.local.datasources.data_store.PreferencesDataStoreImpl
import com.example.budgetly.data.local.datasources.db.api.assistant.AssistantResponseDataSource
import com.example.budgetly.data.local.datasources.db.api.assistant.AssistantResponseDataSourceImpl
import com.example.budgetly.data.local.datasources.db.api.assistant.ChatDataSource
import com.example.budgetly.data.local.datasources.db.api.assistant.ChatDataSourceImpl
import com.example.budgetly.data.local.datasources.db.api.nordigen.LocalDataSource
import com.example.budgetly.data.local.datasources.db.api.nordigen.LocalDataSourceImpl
import com.example.budgetly.data.local.datasources.db.local.account.AccountDataSource
import com.example.budgetly.data.local.datasources.db.local.account.AccountDataSourceImpl
import com.example.budgetly.data.local.datasources.db.local.category.CategoryDataSource
import com.example.budgetly.data.local.datasources.db.local.category.CategoryDataSourceImpl
import com.example.budgetly.data.local.datasources.db.local.notification.NotificationDataSource
import com.example.budgetly.data.local.datasources.db.local.notification.NotificationDataSourceImpl
import com.example.budgetly.data.local.datasources.db.local.subcategory.SubCategoryDataSource
import com.example.budgetly.data.local.datasources.db.local.subcategory.SubCategoryDataSourceImpl
import com.example.budgetly.data.local.datasources.db.local.transaction.TransactionDataSource
import com.example.budgetly.data.local.datasources.db.local.transaction.TransactionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
//    @Singleton
    abstract fun bindsLocalDataSource(impl: LocalDataSourceImpl): LocalDataSource

    @Binds
    abstract fun bindsPreferenceDataStore(impl: PreferencesDataStoreImpl): PreferenceDataStore



    @Binds
//    @Singleton
    abstract fun bindsCategoryDataSource(impl: CategoryDataSourceImpl): CategoryDataSource

    @Binds
//    @Singleton
    abstract fun bindsNotificationDataSource(impl: NotificationDataSourceImpl): NotificationDataSource

    @Binds
//    @Singleton
    abstract fun bindsChatDataSource(impl: ChatDataSourceImpl): ChatDataSource

    @Binds
//    @Singleton
    abstract fun bindsAssistantResponseDataSource(impl: AssistantResponseDataSourceImpl): AssistantResponseDataSource

    @Binds
//    @Singleton
    abstract fun bindsSubCategoryDataSource(impl: SubCategoryDataSourceImpl): SubCategoryDataSource
    @Binds
//    @Singleton
    abstract fun bindsAccountDataSource(impl: AccountDataSourceImpl): AccountDataSource
    @Binds
//    @Singleton
    abstract fun bindsTransactionDataSource(impl: TransactionDataSourceImpl): TransactionDataSource

}
