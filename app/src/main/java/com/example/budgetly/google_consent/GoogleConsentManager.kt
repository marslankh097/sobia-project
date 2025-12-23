package com.example.budgetly.google_consent

import android.app.Activity
import android.content.Context
import android.provider.Settings
import com.demo.budgetly.BuildConfig
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleConsentManager @Inject constructor(
    context: Context
) {
    var consentTimeOut = 8L
    private var consentInformation = UserMessagingPlatform.getConsentInformation(context)

    private val debugSettings =  ConsentDebugSettings.Builder(context)
        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        .addTestDeviceHashedId(context.admobDeviceHash())
        .build()

    val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()
    fun requestConsent(
        activity: Activity,
        callback: (ConsentResult) -> Unit,
    ) {
        // 1️⃣ If ads can already be requested → return immediately
        if (consentInformation.canRequestAds()) {
            callback(ConsentResult.Granted)
            return
        }

        // 2️⃣ Build params with test device hash
        val params = ConsentRequestParameters.Builder()
            .setConsentDebugSettings(if (BuildConfig.DEBUG) debugSettings else null)
            .setTagForUnderAgeOfConsent(false)
            .build()

        // 3️⃣ Request consent info update
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                // Success
                if (!consentInformation.isConsentFormAvailable) {
                    callback(ConsentResult.NotRequired)
                    return@requestConsentInfoUpdate
                }

                // 4️⃣ Load & show form with timeout
                loadAndShowConsentFormWithTimeout(activity, callback, consentInformation)
            },
            { error ->
                // Failure → continue anyway
                callback(ConsentResult.Error("Consent info update failed: ${error.errorCode}"))
            }
        )
    }

    private fun loadAndShowConsentFormWithTimeout(
        activity: Activity,
        callback: (ConsentResult) -> Unit,
        consentInfo: ConsentInformation
    ) {
        var timeoutOccurred = false
        val handler = android.os.Handler(android.os.Looper.getMainLooper())

        val timeoutRunnable = Runnable {
            timeoutOccurred = true
            callback(ConsentResult.Timeout)
        }

        handler.postDelayed(timeoutRunnable, (consentTimeOut * 1000))

        UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
            // Cancel timeout
            handler.removeCallbacks(timeoutRunnable)

            if (timeoutOccurred) return@loadAndShowConsentFormIfRequired

            if (formError != null) {
                callback(ConsentResult.Error("Consent form error: ${formError.message}"))
            } else {
                val status = consentInfo.consentStatus
                val result = when {
                    consentInfo.canRequestAds() -> ConsentResult.Granted
                    status == ConsentInformation.ConsentStatus.NOT_REQUIRED -> ConsentResult.NotRequired
                    else -> ConsentResult.Rejected
                }
                callback(result)
            }
        }
    }
    private fun Context.admobDeviceHash(): String = try {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val md5 = MessageDigest.getInstance("MD5").digest(androidId.toByteArray())
        md5.joinToString("") { "%02x".format(it) }.uppercase()
    } catch (e: Exception) {
        ""
    }
}

