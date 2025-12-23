package com.example.budgetly.utils.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.secondaryBgColor

@Composable
fun AppExitDialog(
    modifier: Modifier = Modifier,
    title: String,
    msg: String,
    positiveText: String,
    negativeText: String,
    onButtonClick: (Boolean) -> Unit
) {
    BaseDialog(onButtonClick = onButtonClick) {
        Box(
            modifier = modifier.fillMaxSize().background(secondaryBgColor), contentAlignment = Alignment.BottomCenter
        ) {
            DialogContent(
                modifier = modifier,
                title = title,
                msg = msg,
                positiveText = positiveText,
                negativeText = negativeText,
                onButtonClick = onButtonClick
            )
            VerticalSpacer(20)
        }
    }
}