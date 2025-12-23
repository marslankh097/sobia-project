package com.example.budgetly.ui.upload_receipt.screens.camera_gallery.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CameraGalleryCard(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    bgColor: Color = itemBgColor,
    onCardClick: () -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier
            .fillMaxWidth().height(100.sdp)
            .safeClickAble {
                onCardClick.invoke()
            },
        bgColor = bgColor,
        borderColor = greyShade1,
        borderRadius = 8f,
        borderThickness = 1f
    ) {
        Column(
            modifier = modifier.fillMaxHeight().padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
                Image(
                    painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(40.sdp)
                )
               VerticalSpacer()
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = SubtitleMedium,
                    color = textColor,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
        }
    }
}