package com.example.budgetly.utils.dialog

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.LabelLarge
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.TypoH6
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.grey
import com.example.budgetly.utils.inactiveButtonColor
import com.example.budgetly.utils.inactiveTextColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.SecondaryButtonRounded
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    title: String,
    msg: String,
    positiveText: String,
    negativeText: String,
    onButtonClick: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.sdp, vertical = 20.sdp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = secondaryColor,
            style = TypoH6,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 10.sdp)
        )
        VerticalSpacer(12)
        Text(
            text = msg,
            style = SubtitleLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 10.sdp),
            color = textColor,
        )
        VerticalSpacer(20)
        Row(modifier = Modifier.fillMaxWidth()) {
            SecondaryButtonRounded(modifier = Modifier
                .weight(1f),
                horizontalPadding = 10,
                transparentColors = listOf(inactiveButtonColor, inactiveButtonColor),
                gradientColors = listOf(inactiveTextColor, inactiveTextColor),
                onButtonClick = { onButtonClick.invoke(false) }) {
                Text(
                    modifier = Modifier.scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Horizontal,
                    ), text = negativeText, color = grey, style = ButtonMedium,
                )
            }
            PrimaryButtonRounded(modifier = Modifier
                .weight(1f),
                horizontalPadding = 10,
                onButtonClick = { onButtonClick.invoke(true) }) {
                Text(text = positiveText, color = white, style = LabelLarge)
            }
        }
    }
}