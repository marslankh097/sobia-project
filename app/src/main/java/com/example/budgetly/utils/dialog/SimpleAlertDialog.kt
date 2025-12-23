package com.example.budgetly.utils.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SimpleAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    msg: String,
    positiveText: String,
    negativeText: String,
    onButtonClick: (Boolean) -> Unit
) {
    BaseDialog(onButtonClick = onButtonClick) {
        RoundedBorderRectangle(
            modifier = modifier.padding(12.sdp),
            bgColor = itemBgColor,
            borderColor = Color.Transparent,
            borderRadius = 8f, borderThickness = 1f
        ) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogContent(
                    modifier = modifier,
                    title = title,
                    msg = msg,
                    positiveText = positiveText,
                    negativeText = negativeText,
                    onButtonClick = onButtonClick
                )
            }
        }
    }
}