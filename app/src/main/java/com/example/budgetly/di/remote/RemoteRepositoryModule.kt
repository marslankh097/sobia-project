package com.example.budgetly.di.remote

import com.example.budgetly.data.repositories_impl.api_repo_impl.banking.AccountRepositoryImpl
import com.example.budgetly.data.repositories_impl.api_repo_impl.banking.AgreementRepositoryImpl
import com.example.budgetly.data.repositories_impl.api_repo_impl.banking.InstitutionRepositoryImpl
import com.example.budgetly.data.repositories_impl.api_repo_impl.banking.RequisitionRepositoryImpl
import com.example.budgetly.data.repositories_impl.api_repo_impl.banking.TokenRepositoryImpl
import com.example.budgetly.data.repositories_impl.api_repo_impl.banking.TransactionRepositoryImpl
import com.example.budgetly.data.repositories_impl.api_repo_impl.pinecone.PineConeRepositoryImpl
import com.example.budgetly.data.repositories_impl.api_repo_impl.receipt.ReceiptRepositoryImpl
import com.example.budgetly.data.repositories_impl.system_repo_impl.RemoteConfigRepositoryImpl
import com.example.budgetly.domain.repositories.api.banking.AccountRepository
import com.example.budgetly.domain.repositories.api.banking.AgreementRepository
import com.example.budgetly.domain.repositories.api.banking.InstitutionRepository
import com.example.budgetly.domain.repositories.api.banking.RequisitionRepository
import com.example.budgetly.domain.repositories.api.banking.TokenRepository
import com.example.budgetly.domain.repositories.api.banking.TransactionRepository
import com.example.budgetly.domain.repositories.api.pinecone.PineConeRepository
import com.example.budgetly.domain.repositories.api.receipt.ReceiptRepository
import com.example.budgetly.domain.repositories.system.RemoteConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(
        impl: RemoteConfigRepositoryImpl
    ): RemoteConfigRepository

    @Binds
    @Singleton
    abstract fun bindsTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository
    @Binds
    @Singleton
    abstract fun bindsAccountRepository(impl: AccountRepositoryImpl): AccountRepository
    @Binds
    @Singleton
    abstract fun bindsPineConeRepository(impl: PineConeRepositoryImpl): PineConeRepository
    
    @Binds
    @Singleton
    abstract fun bindTokenRepository(impl: TokenRepositoryImpl): TokenRepository

    @Binds
    @Singleton
    abstract fun bindInstitutionRepository(
        impl: InstitutionRepositoryImpl
    ): InstitutionRepository

    @Binds
    @Singleton
    abstract fun bindReceiptRepository(
        impl: ReceiptRepositoryImpl
    ): ReceiptRepository

    @Binds
    @Singleton
    abstract fun bindAgreementRepository(
        impl: AgreementRepositoryImpl
    ): AgreementRepository

    @Binds
    @Singleton
    abstract fun bindRequisitionRepository(
        impl: RequisitionRepositoryImpl
    ): RequisitionRepository
}
