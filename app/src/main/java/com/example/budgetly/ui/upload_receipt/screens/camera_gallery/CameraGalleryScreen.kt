package com.example.budgetly.ui.upload_receipt.screens.camera_gallery

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.BannerAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.upload_receipt.ReceiptViewModel
import com.example.budgetly.ui.upload_receipt.events.ReceiptEvent
import com.example.budgetly.ui.upload_receipt.screens.camera_gallery.content.CameraGalleryCard
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.permissions.goToAppSettings
import com.example.budgetly.utils.permissions.isCameraPermissionGranted
import com.example.budgetly.utils.permissions.isCameraPermissionPermanentlyDenied
import com.example.budgetly.utils.permissions.isReadStoragePermissionGranted
import com.example.budgetly.utils.permissions.isReadStoragePermissionPermanentlyDenied
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toast
import ir.kaaveh.sdpcompose.sdp
import java.io.File

@Composable
fun CameraGalleryScreen(
    modifier: Modifier = Modifier,
    onImagePicked: () -> Unit,
    navigateToReceiptHistory:() ->Unit,
    receiptViewModel: ReceiptViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }

    val displaySettingPermissionDialog by receiptViewModel.displaySettingDialog.collectAsState()
    val context = LocalContext.current

    // Create camera file & uri
    val photoFile = remember {
        File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
    }
    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            receiptViewModel.onEvent(ReceiptEvent.UploadUri(photoUri))
            receiptViewModel.onEvent(ReceiptEvent.UploadFileFromUri(context, true))
            onImagePicked()
        }
    }

    // ─────── Gallery Launcher ───────
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            receiptViewModel.onEvent(ReceiptEvent.UploadUri( it)) // ViewModel will convert to file
            receiptViewModel.onEvent(ReceiptEvent.UploadFileFromUri(context)) // ViewModel will convert to file
            onImagePicked()
        }
    }

    // ─────── Camera Permission ───────
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(photoUri)
        } else {
            if (context.isCameraPermissionPermanentlyDenied()) {
                receiptViewModel.onEvent(ReceiptEvent.DisplaySettingPermissionDialog(true))
                receiptViewModel.onEvent(ReceiptEvent.SetSettingDialogType(true))
            } else {
                context.toast("Camera permission denied")
            }
        }
    }

    // ─────── Gallery Permission ───────
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            if (context.isReadStoragePermissionPermanentlyDenied()) {
                receiptViewModel.onEvent(ReceiptEvent.DisplaySettingPermissionDialog(true))
                receiptViewModel.onEvent(ReceiptEvent.SetSettingDialogType(false))
            } else {
                context.toast("Storage permission denied")
            }
        }
    }

    // ─────── Settings Launcher ───────
    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (displaySettingPermissionDialog.second) {
            if (context.isCameraPermissionGranted()) {
                cameraLauncher.launch(photoUri)
            }
        } else {
            if (context.isReadStoragePermissionGranted()) {
                galleryLauncher.launch("image/*")
            }
        }
    }

    // ─────── Dialog ───────
    if (displaySettingPermissionDialog.first) {
        val isCamera = displaySettingPermissionDialog.second
        val title = if (isCamera) stringResource(R.string.camera_permission)
        else stringResource(R.string.storage_permission)
        val message = if (isCamera) stringResource(R.string.you_need_to_grant_camera_permission_from_the_settings)
        else stringResource(R.string.you_need_to_grant_storage_permission_from_the_settings)

        SimpleAlertDialog(
            title = title,
            msg = message,
            positiveText = "Grant",
            negativeText = stringResource(R.string.cancel)
        ) {
            if (it) context.goToAppSettings(settingsLauncher)
            receiptViewModel.onEvent(ReceiptEvent.DisplaySettingPermissionDialog(false))
        }
    }

    // ─────── UI Layout ───────
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(title = stringResource(R.string.upload_receipt),
            icon3 = R.drawable.icon_history,
            onClickIcon3 = {
                navigateToReceiptHistory()
            },
            onClick = navigateBack)

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CameraGalleryCard(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.img_camera,
                    title = stringResource(R.string.camera)
                ) {
                    if (context.isCameraPermissionGranted()) {
                        cameraLauncher.launch(photoUri)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }

                HorizontalSpacer()

                CameraGalleryCard(
                    modifier = Modifier.weight(1f),
                    icon = R.drawable.img_gallery,
                    title = stringResource(R.string.gallery)
                ) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        if (context.isReadStoragePermissionGranted()) {
                            galleryLauncher.launch("image/*")
                        } else {
                            galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    } else {
                        galleryLauncher.launch("image/*") // API 29+ doesn't require permission
                    }
                }
            }
        }
        VerticalSpacer()
        BannerAd(
            context = context as Activity,
            adKey = AdKeys.ReceiptBanner.name,
            placementKey = RemoteConfigKeys.RECEIPT_BANNER_ENABLED,
            screenName = "Receipt_Camera_Gallery_Bottom"
        )
        VerticalSpacer(5)
    }
}