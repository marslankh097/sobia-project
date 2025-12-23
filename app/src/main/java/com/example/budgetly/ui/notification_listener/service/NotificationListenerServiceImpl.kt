package com.example.budgetly.ui.notification_listener.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.budgetly.domain.models.db.notification.NotificationModel
import com.example.budgetly.domain.usecases.db_usecases.notification.SaveNotificationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListenerServiceImpl : NotificationListenerService() {

    @Inject lateinit var coroutineScope: CoroutineScope
    @Inject lateinit var saveNotificationUseCase: SaveNotificationUseCase

    private val recentMessages = mutableSetOf<String>()

    // 🔒 Allowed sender lists
    private val allowedSmsSenders = listOf("8558", "14250", "18258")
    private val allowedEmailSenders = listOf("alerts@hbl.com", "transaction@mcb.com")
    private val allowedWhatsAppSenders = listOf("HBL", "UBL", "Jazz","Easypaisa", "JazzCash", "Telenor")

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        val packageName = sbn.packageName
        val notification = sbn.notification

        // 🚫 Skip group summaries
        if ((notification.flags and Notification.FLAG_GROUP_SUMMARY) != 0) return

        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE)?.trim().orEmpty()
        val message = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()?.trim().orEmpty()
        val timestamp = sbn.postTime

        if (title.isBlank() || message.isBlank()) return

        if(isAllowedNotificationPackage(packageName).not()) return

        if(isWhatsApp(packageName) && isJunkWhatsApp(message)) return
        if(isEmail(packageName) && isSpamEmailContent(title,message)) return

        // 🔍 Main routing logic
//        val isRelevant = when {
//            isSMS(packageName) -> isRelevantSms(title, message)
//            isEmail(packageName) -> isRelevantEmail(title, message)
//            isWhatsApp(packageName) -> isRelevantWhatsApp(title, message)
//            else -> false
//        }
//        if (!isRelevant) return

        // 🔁 Deduplication
        val dedupKey = "$packageName|$title|$message"
        if (recentMessages.contains(dedupKey)) return
        recentMessages.add(dedupKey)
        coroutineScope.launch {
            delay(5000)
            recentMessages.remove(dedupKey)
        }

        // ✅ Save notification
        val notificationModel = NotificationModel(
            id = 0,
            title = title,
            message = message,
            pkgName = packageName,
            appName = resolveAppName(packageName),
            timeInMillis = timestamp
        )

        coroutineScope.launch {
            saveNotificationUseCase(notificationModel)
        }
    }

    private fun isRelevantSms(sender: String, message: String): Boolean {
        return allowedSmsSenders.any { it.equals(sender, ignoreCase = true) } || isFinancialMessage(message)
    }


    private fun isRelevantEmail(sender: String, message: String): Boolean {
        return allowedEmailSenders.any { sender.contains(it, ignoreCase = true) } || isFinancialMessage(message)
    }


    private fun isRelevantWhatsApp(sender: String, message: String): Boolean {
        if (isJunkWhatsApp(message)) return false

        return allowedWhatsAppSenders.any { sender.contains(it, ignoreCase = true) } || isFinancialMessage(message)
    }
    private fun isAllowedNotificationPackage(packageName: String): Boolean {
        return packageName.contains("com.google.android.gm") ||
                packageName.contains("com.google.android.apps.messaging") ||
                packageName.contains("com.samsung.android.messaging") ||
                packageName.contains("com.whatsapp")
    }



    // ✅ Core financial message detection
    private fun isFinancialMessage(message: String): Boolean {
        val keywords = listOf(
            "debited", "credited", "transaction", "txn", "rs.", "inr",
            "upi", "payment", "withdrawn", "deposit", "otp", "balance", "account"
        )
        val lower = message.lowercase()
        return keywords.any { lower.contains(it) }
    }

    // ✅ WhatsApp-specific junk filter
    private fun isJunkWhatsApp(message: String): Boolean {
        val lower = message.lowercase()
        return (lower.contains("reacted") && lower.contains("to \"")) ||
                lower.contains("new messages")
    }

    private fun resolveAppName(packageName: String): String = when (packageName) {
        "com.google.android.apps.messaging", "com.samsung.android.messaging" -> "SMS"
        "com.google.android.gm" -> "Gmail"
        "com.whatsapp" -> "WhatsApp"
        else -> packageName
    }
    private fun isSpamEmailContent(title: String, message: String): Boolean {
        val spamKeywords = listOf(
            "limited time", "exclusive deal", "promo", "offer", "subscribe",
            "discount", "free gift", "act now", "win", "reward", "click here"
        )

        val content = ("$title $message").lowercase()
        return spamKeywords.any { content.contains(it) }
    }

    private fun isWhatsApp(packageName: String): Boolean {
        return packageName.startsWith("com.whatsapp")
    }
    private fun isSMS(packageName: String): Boolean {
        return packageName in listOf("com.google.android.apps.messaging","com.samsung.android.messaging")
    }

    private fun isEmail(packageName: String): Boolean {
        return packageName in listOf("com.google.android.gm")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {}
}


