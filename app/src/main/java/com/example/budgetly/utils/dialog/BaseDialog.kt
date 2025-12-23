package com.example.budgetly.utils.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun  BaseDialog(onButtonClick:(Boolean)->Unit,dismissOnClickOutside:Boolean = false,
                dismissOnBackPress:Boolean = false,
                content: @Composable () -> Unit) {
    Dialog(
        onDismissRequest = { onButtonClick.invoke(false) },
        properties = DialogProperties(
            dismissOnClickOutside = dismissOnClickOutside,
            dismissOnBackPress = dismissOnBackPress,
            usePlatformDefaultWidth = false
        )
    ) {
        content()
    }
}