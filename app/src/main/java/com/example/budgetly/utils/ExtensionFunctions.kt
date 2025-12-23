package com.example.budgetly.utils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RawRes
import com.demo.budgetly.BuildConfig
import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency
import com.example.budgetly.domain.models.enums.transaction.TransactionType
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


fun <T> Collection<T>.firstEntry(): T? {
    return if (this.isEmpty())
        null
    else this.last()
}
// Long to formatted date string (e.g., "2025-05-14")
fun Long.toDateString(pattern: String = "yyyy-MM-dd"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date(this))
}
fun Context.clearCacheDirectory() {
    val cacheDir: File = this.cacheDir
    if (cacheDir.isDirectory) {
        val files: Array<String> = cacheDir.list() ?: return
        for (file in files) {
            File(cacheDir, file).delete()
        }
    }
}
fun Uri.toFileSafely(context: Context): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this) ?: return null
        val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        tempFile
    } catch (e: Exception) {
        null
    }
}
fun Uri.toCompressedFile(context: Context, quality: Int = 90): File? {
    return try {
        // Load bitmap from URI
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }

        // Create temp file
        val tempFile = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")

        // Compress bitmap to JPEG with given quality
        FileOutputStream(tempFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String.toDateString(pattern: String = "dd MMM yyyy", alreadyPattern:String = "dd MMM yyyy"): String {
    val date  = this.toDateLong(alreadyPattern)
    return date.toDateString(pattern)
}
// Formatted date string to Long (milliseconds since epoch)
fun String.toDateLong(pattern: String = "yyyy-MM-dd"): Long {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(this)?.time ?: 0L
}
fun String.toComplexDateLong(): Long {
    val formats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", // microseconds
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",    // milliseconds
        "yyyy-MM-dd'T'HH:mm:ss'Z'",        // seconds only
        "yyyy-MM-dd"                       // fallback for date only
    )

    for (format in formats) {
        try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            return sdf.parse(this)?.time ?: 0L
        } catch (_: Exception) {
            // try next format
        }
    }
    return 0L // fallback
}
fun String.isIncome():Boolean{
   return this.toDoubleOrNull()?.let { it > 0 } ?: false
}
fun Context.toast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}
fun log(message: String, tag:String = "exception"){
    Log.d(tag,message)
}

fun Context.setAnalytics(event: String) {
    var message = event
    if (message.contains(" ")) {
        message = message.replace(" ", "_")
    }

    if (message.length > 40) {
        message = message.substring(0, 40)
    }
    if (!BuildConfig.DEBUG) {
        try {
            FirebaseAnalytics.getInstance(this).logEvent("" + message, Bundle())
        } catch (ignored: Exception) {
        }
    }else{
        log(message)
    }
}
fun String?.toSafeEmptyString(): String {
    return if(this.isNullOrBlank()) "____" else this
}
fun String.toSafeDouble():Double {
    return try {
        this.toDouble()
    }catch (e:java.lang.Exception){
        0.0
    }
}
fun Context.getRawResourceUri(@RawRes resId: Int ): Uri {
    return Uri.parse("android.resource://${this.packageName}/$resId")
}
fun Context.getRawResourceString(@RawRes resId: Int ): String {
    return "android.resource://${this.packageName}/$resId"
}


fun <T> Collection<T>.lastEntry(): T? {
    return if (this.isEmpty())
        null
    else this.last()
}
fun String.toTransactionType(): TransactionType {
    return enumValueOf<TransactionType>(this)
}
fun String.toTransactionFrequency(): TransactionFrequency {
    return enumValueOf<TransactionFrequency>(this)
}