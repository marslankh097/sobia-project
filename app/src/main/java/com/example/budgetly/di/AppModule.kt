package com.example.budgetly.di
import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import com.example.appupdate.AppUpdateHelper
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.MyBannerManager
import com.example.budgetly.ads.MyInterManager
import com.example.budgetly.ads.MyNativeManager
import com.example.budgetly.domain.usecases.system_usecases.PreferenceUseCases
import com.example.budgetly.google_consent.GoogleConsentManager
import com.example.budgetly.utils.copy_controller.CopyController
import com.example.budgetly.utils.copy_controller.CopyControllerImpl
import com.example.budgetly.utils.internet_controller.InternetController
import com.example.budgetly.utils.internet_controller.InternetControllerImpl
import com.google.gson.Gson
import com.monetization.adsmain.sdk.AdmobAdsSdk
import com.monetization.adsmain.splash.AdmobSplashAdController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClipboardManager(
        @ApplicationContext context: Context
    ): ClipboardManager {
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    @Provides
    @Singleton
    fun getAdsSdk() = AdmobAdsSdk()
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
    @Provides
    @Singleton
    fun provideCopyController(
        @ApplicationContext context: Context,
        clipboardManager: ClipboardManager
    ): CopyController = CopyControllerImpl(context,clipboardManager)

    @Provides
    @Singleton
    fun provideInternetController(
        @ApplicationContext context: Context,
        connectivityManager: ConnectivityManager
    ): InternetController = InternetControllerImpl(connectivityManager)


    @Provides
    @Singleton
    fun provideGoogleConsentManager(
        @ApplicationContext context: Context
    ): GoogleConsentManager = GoogleConsentManager(context)
    @Provides
    @Singleton
    fun provideCommonAdsUtil(
        consent: GoogleConsentManager,
        preferenceUseCases: PreferenceUseCases,
        internetController: InternetController,
    ): CommonAdsUtil = CommonAdsUtil(consent,preferenceUseCases,internetController)

    @Provides
    @Singleton
    fun getAdmobSplashAdController() = AdmobSplashAdController()

    @Provides
    @Singleton
    fun getAppUpdateHelper() = AppUpdateHelper()

    @Provides
    @Singleton
    fun getContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Provides
    @Singleton
    fun provideMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun getMyInterManager(commonAdsUtil:CommonAdsUtil) = MyInterManager(commonAdsUtil)

    @Provides
    @Singleton
    fun getMyNativeManager() = MyNativeManager()

    @Provides
    @Singleton
    fun getMyBannerManager() = MyBannerManager()
//    @Provides
//    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

//    @Provides
//    fun provideRemoteConfigRepository(remoteConfig: FirebaseRemoteConfig): RemoteConfigRepository =
//        RemoteConfigRepositoryImpl(remoteConfig)

}
