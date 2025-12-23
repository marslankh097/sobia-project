package com.example.budgetly.utils.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.budgetly.utils.toast

fun Context.goToAppSettings(launcher: ManagedActivityResultLauncher<Intent, ActivityResult>) {
    return  try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        launcher.launch(intent)
    }catch (e:Exception){
        toast("Couldn't Launch App Settings.")
    }
}

fun Context.isCameraPermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}
fun Context.isReadStoragePermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}

fun Context.isCameraPermissionPermanentlyDenied(): Boolean {
    return  try {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this.findActivity(),
                    Manifest.permission.CAMERA
                )
    } catch (e:Exception){
            toast("Context is not an Activity.")
            false
        }
}

fun Context.isReadStoragePermissionPermanentlyDenied(): Boolean {
    return  try {
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                !ActivityCompat.shouldShowRequestPermissionRationale(this.findActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
    }catch (e:Exception){
        toast("Context is not an Activity.")
        false
    }
}
fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("Context is not an Activity.")
}

fun Context.isNotificationServiceEnabled(): Boolean {
    val enabledPackages = Settings.Secure.getString(
        this.contentResolver,
        "enabled_notification_listeners"
    )
    return enabledPackages?.contains(this.packageName) == true
}

fun notificationListenerIntent(launcher: (Intent) -> Unit) {
    launcher.invoke(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
}