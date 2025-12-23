package com.example.budgetly.utils.copy_controller

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import com.example.budgetly.utils.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CopyControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clipboard:ClipboardManager
) : CopyController {

    override fun copy(text: AnnotatedString) {
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        context.toast("Copied to clipboard")
    }
}
