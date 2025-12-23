package com.example.budgetly.utils.shared_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SimpleRow(
    modifier: Modifier = Modifier,
    imageId: Int = -1,
    imageSize: Int = 24,
    title: String,
    style: TextStyle = SubtitleSmall,
    bgColor: Color = itemBgColor,
    applyTint:Boolean = false,
    tintColor: Color = primaryColor,
    txtColor: Color = textColor,
    paddingValues: PaddingValues = PaddingValues(horizontal = 12.sdp, vertical = 8.sdp),
    horizontalSpacer: Int = 10,
    isCenter: Boolean = false,
    onClick:(()->Unit) ?= null
) {
    Row(
        modifier = modifier
            .fillMaxWidth().background(bgColor)
            .padding(paddingValues).safeClickAble {
                if (onClick != null) {
                    onClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isCenter) Arrangement.Center else Arrangement.Start
    ) {
        if (imageId != -1) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = "",
                modifier = Modifier
                    .size(imageSize.sdp),
                colorFilter = if(applyTint) ColorFilter.tint(tintColor) else null
            )
        }
        HorizontalSpacer(horizontalSpacer)
        Text(
            text = title,
            style = style,
            textAlign = TextAlign.Start,
            color = txtColor
        )
    }
}