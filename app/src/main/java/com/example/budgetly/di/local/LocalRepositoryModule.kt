package com.example.budgetly.di.local
import com.example.budgetly.data.repositories_impl.api_repo_impl.assistant.AssistantRepositoryImpl
import com.example.budgetly.data.repositories_impl.db_repo_impl.cash.AccountRepositoryImpl
import com.example.budgetly.data.repositories_impl.db_repo_impl.cash.CategoryRepositoryImpl
import com.example.budgetly.data.repositories_impl.db_repo_impl.cash.SubCategoryRepositoryImpl
import com.example.budgetly.data.repositories_impl.db_repo_impl.cash.TransactionRepositoryImpl
import com.example.budgetly.data.repositories_impl.db_repo_impl.notification.NotificationRepositoryImpl
import com.example.budgetly.data.repositories_impl.system_repo_impl.PreferenceRepositoryImpl
import com.example.budgetly.domain.repositories.api.assistant.AssistantRepository
import com.example.budgetly.domain.repositories.db.cash.AccountRepository
import com.example.budgetly.domain.repositories.db.cash.CategoryRepository
import com.example.budgetly.domain.repositories.db.cash.SubCategoryRepository
import com.example.budgetly.domain.repositories.db.cash.TransactionRepository
import com.example.budgetly.domain.repositories.db.notification.NotificationRepository
import com.example.budgetly.domain.repositories.system.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl
    ): PreferenceRepository

    @Binds
    @Singleton
    abstract fun bindsTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository
    @Binds
    @Singleton
    abstract fun bindsNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
    @Binds
    @Singleton
    abstract fun bindsAccountRepository(impl: AccountRepositoryImpl): AccountRepository
    @Binds
    @Singleton
    abstract fun bindsSubCategoryRepository(impl: SubCategoryRepositoryImpl): SubCategoryRepository
    @Binds
    @Singleton
    abstract fun bindsAssistantRepository(impl: AssistantRepositoryImpl): AssistantRepository
    @Binds
    @Singleton
    abstract fun bindsCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}
