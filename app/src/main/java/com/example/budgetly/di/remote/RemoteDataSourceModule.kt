package com.example.budgetly.di.remote
import com.example.budgetly.data.remote.datasources.retrofit.AccountDataSource
import com.example.budgetly.data.remote.datasources.retrofit.AccountDataSourceImpl
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigDataSource
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigDataSourceImpl
import com.example.budgetly.data.remote.datasources.retrofit.AgreementDataSource
import com.example.budgetly.data.remote.datasources.retrofit.AgreementDataSourceImpl
import com.example.budgetly.data.remote.datasources.retrofit.InstitutionDataSource
import com.example.budgetly.data.remote.datasources.retrofit.InstitutionDataSourceImpl
import com.example.budgetly.data.remote.datasources.retrofit.RequisitionDataSource
import com.example.budgetly.data.remote.datasources.retrofit.RequisitionDataSourceImpl
import com.example.budgetly.data.remote.datasources.retrofit.TokenDataSource
import com.example.budgetly.data.remote.datasources.retrofit.TokenDataSourceImpl
import com.example.budgetly.data.remote.datasources.retrofit.assistant.AssistantDataSource
import com.example.budgetly.data.remote.datasources.retrofit.assistant.AssistantDataSourceImpl
import com.example.budgetly.data.remote.datasources.retrofit.pinecone.PineConeDataSource
import com.example.budgetly.data.remote.datasources.retrofit.pinecone.PineConeDataSourceImpl
import com.example.budgetly.data.remote.datasources.retrofit.receipt.ReceiptRemoteDataSource
import com.example.budgetly.data.remote.datasources.retrofit.receipt.ReceiptRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {
//    @Binds
//    @Singleton
//    abstract fun bindRemoteDataSource(
//        impl: RemoteDataSourceImpl
//    ): RemoteDataSource

    @Binds
    abstract fun bindPineConeDataSource(
        impl: PineConeDataSourceImpl
    ): PineConeDataSource

    @Binds
    abstract fun bindAssistantDataSource(
        impl: AssistantDataSourceImpl
    ): AssistantDataSource


    @Binds
    abstract fun bindTokenDataSource(
        impl: TokenDataSourceImpl
    ): TokenDataSource

    @Binds
    abstract fun bindInstitutionDataSource(
        impl: InstitutionDataSourceImpl
    ): InstitutionDataSource

    @Binds
    abstract fun bindAccountDataSource(
        impl: AccountDataSourceImpl
    ): AccountDataSource

    @Binds
    abstract fun bindAgreementDataSource(
        impl: AgreementDataSourceImpl
    ): AgreementDataSource

    @Binds
    abstract fun bindRequisitionDataSource(
        impl: RequisitionDataSourceImpl
    ): RequisitionDataSource

    @Binds
    abstract fun bindReceiptRemoteDataSource(
        impl: ReceiptRemoteDataSourceImpl
    ): ReceiptRemoteDataSource

    @Binds
    abstract fun bindRemoteConfigDataSource(impl: RemoteConfigDataSourceImpl): RemoteConfigDataSource
}
