package com.example.budgetly.utils.shared_components
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.bgShade2
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ImageSwitchRow(
    modifier: Modifier = Modifier,
    imageId1: Int,
    imageId2: Int,
    tabText1: String,
    tabText2: String,
    switchOn: Boolean = false,
    switchChanged: (Boolean) -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier,
        bgColor = bgShade2,
        borderColor = secondaryColor,
        borderRadius = 27f, borderThickness = 1f
    ) {
        Row(
            modifier = modifier.padding(horizontal = 4.sdp, vertical = 4.sdp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageRowBox(
                modifier = Modifier,
                tabText = tabText1, imageId = imageId1, selectedTab = !switchOn, onClick = {
                    switchChanged.invoke(false)
                })
            ImageRowBox(
                modifier = Modifier,
                tabText = tabText2, imageId = imageId2, selectedTab = switchOn, onClick = {
                    switchChanged.invoke(true)
                })
        }
    }
}

@Composable
fun ImageRowBox(
    modifier: Modifier = Modifier,
    imageId: Int,
    tabText: String,
    selectedTab: Boolean,
    onClick: () -> Unit) {
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble { onClick.invoke() },
        bgColor = if(selectedTab) secondaryColor else Color.Transparent,
        borderColor = Color.Transparent,
        borderRadius = 20f, borderThickness = 0f
    ) {
        Row(
            modifier = modifier.padding(vertical = 10.sdp, horizontal = 16.sdp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(16.sdp),
                painter = painterResource(id = imageId),
                contentDescription = "",
                colorFilter = ColorFilter.tint(if (selectedTab) white else hintColor)
            )
            HorizontalSpacer(5)
            Text(
                text = tabText, style = SubtitleSmall,
                color =
                    if (selectedTab) white else hintColor,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
        }
    }
}