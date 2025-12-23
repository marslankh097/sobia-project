package com.example.budgetly.di.remote
import com.example.budgetly.data.remote.api.nordigen.AccountApi
import com.example.budgetly.data.remote.api.nordigen.AgreementApi
import com.example.budgetly.data.remote.api.nordigen.InstitutionApi
import com.example.budgetly.data.remote.api.nordigen.RequisitionApi
import com.example.budgetly.data.remote.api.RetrofitFactory
import com.example.budgetly.data.remote.api.assistant.AssistantApiService
import com.example.budgetly.data.remote.api.nordigen.TokenApi
import com.example.budgetly.data.remote.api.pinecone.PineconeApiService
import com.example.budgetly.data.remote.api.receipt.ReceiptApiService
import com.example.budgetly.data.remote.datasources.retrofit.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val BASE_URL_NORDIGEN = "https://bankaccountdata.gocardless.com/api/v2/"
    private val SECRET_ID = "6dfdaee0-095e-4845-8f43-fddf1d3d7921"
    private val SECRET_KEY = "e44b8406a4db7114c2d39a603da3d23b97991c4d0f4184da6631ca932a87209dd640200fad7c95cf429e1c114e187a89a91dd6509a2aaea2855fc1a34a3e169f"

    @Provides
    @Singleton
    fun provideRetrofitFactory(): RetrofitFactory = RetrofitFactory

    @Provides
    @Singleton
    fun provideRetrofit(retrofitFactory: RetrofitFactory): Retrofit =
        retrofitFactory.create(BASE_URL_NORDIGEN)

    private val BASE_URL_PINECONE = "https://prod-1-data.ke.pinecone.io/"
    private val BASE_URL_ASSISTANT = "http://budgetly.trippleapps.com:8003/"
    private val BASE_URL_RECEIPT = "http://budgetly.trippleapps.com:8003/"

    @Provides
    @Singleton
    fun providePineconeApi(): PineconeApiService =
        RetrofitFactory.create(BASE_URL_PINECONE).create(PineconeApiService::class.java)
    @Provides
    @Singleton
    fun provideAssistantApi(): AssistantApiService =
        RetrofitFactory.create(BASE_URL_ASSISTANT).create(AssistantApiService::class.java)
    @Provides
    @Singleton
    fun provideReceiptApi(): ReceiptApiService =
        RetrofitFactory.create(BASE_URL_RECEIPT).create(ReceiptApiService::class.java)

    @Provides
    @Singleton
    fun provideTokenApi(retrofit: Retrofit): TokenApi =
        retrofit.create(TokenApi::class.java)

    @Provides
    @Singleton
    fun provideInstitutionApi(retrofit: Retrofit): InstitutionApi =
        retrofit.create(InstitutionApi::class.java)

    @Provides
    @Singleton
    fun provideAgreementApi(retrofit: Retrofit): AgreementApi =
        retrofit.create(AgreementApi::class.java)


    @Provides
    @Singleton
    fun provideAccountApi(retrofit: Retrofit): AccountApi =
        retrofit.create(AccountApi::class.java)
    @Provides
    @Singleton
    fun provideRequisitionApi(retrofit: Retrofit): RequisitionApi =
        retrofit.create(RequisitionApi::class.java)

    @Provides
    @Singleton
    fun provideTokenProvider(tokenApi: TokenApi): TokenProvider =
        TokenProvider(tokenApi,SECRET_ID, SECRET_KEY)

    @Provides
    @Named("pinecone_api_key")
    fun providePineconeApiKey(): String {
        return "pcsk_4GFDvK_7ZEVnhkV616Ty11k5kSgeDPSNYrihZgyGs1eLAvei29qvS8cXixJ568R56Wxgfo"//BuildConfig.PINECONE_API_KEY

    }

//    @Provides
//    @Singleton
//    fun provideRetrofitFactory(): RetrofitFactory = RetrofitFactory
//
//    @Provides
//    @Singleton
//    fun provideRemoteDataSource(
//        retrofitFactory: RetrofitFactory
//    ): RemoteDataSource = RemoteDataSourceImpl(retrofitFactory)
}
