package com.example.budgetly.ads

//var TestRemoteConfigCheck = true
//var splashTime = 13L
//var consentTime = 8L
//var MainNativeLayoutKey = NativeLayouts.SplitNative.layout
//var IncomeNativeLayoutKey = NativeLayouts.SplitNative.layout
//var ExpenseNativeLayoutKey = NativeLayouts.LargeNative.layout
//var BankingNativeLayoutKey = NativeLayouts.LargeNative.layout
/*
@Singleton
class MyRemoteConfigController {
    private var canRequestFirebaseRemoteConfig = true
    private var firebaseRemoteConfigFetchSuccessfully = false
    fun fetch(onFetched: () -> Unit) {
        if (firebaseRemoteConfigFetchSuccessfully) {
            return
        }
        if (!canRequestFirebaseRemoteConfig) {
            return
        }
        canRequestFirebaseRemoteConfig = false
        try {
            SdkRemoteConfigController.fetchRemoteConfig(
                defaultXml = R.xml.remote_config_defaults,
                fetchOutTimeInSeconds = 8,
                handlerDelayInSeconds = 8,
                callback = object : SdkConfigListener {
                    override fun onDismiss() {
                        assignRemoteConfigs(forUpdate = false)
                        onFetched.invoke()
                        canRequestFirebaseRemoteConfig = true
                    }

                    override fun onFailure(error: String) {

                    }

                    override fun onSuccess() {
                        firebaseRemoteConfigFetchSuccessfully = true
                    }
                },
                onUpdate = {
                    assignRemoteConfigs(forUpdate = true)
                }
            )
        }catch (e:Exception){
            canRequestFirebaseRemoteConfig = true
        }
    }

    private fun assignRemoteConfigs(forUpdate: Boolean) {
        SdkRemoteConfigController.apply {
            TestRemoteConfigCheck = getRemoteConfigBoolean("TestRemoteConfigCheck", def = true)
            splashTime = getRemoteConfigLong(RemoteConfigKeys.SPLASH_TIME, def = 13L)
            consentTime = getRemoteConfigLong(RemoteConfigKeys.CONSENT_TIME, def = 8L)
            MainNativeLayoutKey = getRemoteConfigString(RemoteConfigKeys.MAIN_NATIVE_LAYOUT,NativeLayouts.SplitNative.layout )
            IncomeNativeLayoutKey = getRemoteConfigString(RemoteConfigKeys.INCOME_NATIVE_LAYOUT,NativeLayouts.SplitNative.layout )
            ExpenseNativeLayoutKey = getRemoteConfigString(RemoteConfigKeys.EXPENSE_NATIVE_LAYOUT,NativeLayouts.SplitNative.layout )
            BankingNativeLayoutKey = getRemoteConfigString(RemoteConfigKeys.BANKING_NATIVE_LAYOUT,NativeLayouts.SplitNative.layout )
        }
    }

}*/
