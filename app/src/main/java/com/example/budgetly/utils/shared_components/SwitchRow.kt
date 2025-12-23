package com.example.budgetly.utils.shared_components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.btnGradientColor1
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SwitchRow(
    modifier: Modifier = Modifier,
    appIcon: Drawable,
    appTitle: String,
    style: TextStyle = SubtitleSmall,
    switchChecked: Boolean = false,
    switchEnabled: Boolean = true,
    onSwitchChecked: (Boolean) -> Unit,
    paddingValues: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    borderColors: List<Color> = listOf(btnGradientColor1, secondaryColor)
) {
    GradientBorderRectangle(
        modifier = modifier,
        bgBrush = Brush.linearGradient(listOf(itemBgColor, secondaryBgColor)),
        borderBrush = Brush.linearGradient(borderColors),
        borderRadius = 8f,
        borderThickness = 0.5f
    ) {
        Row(
            modifier = Modifier.padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = appIcon.toBitmap().asImageBitmap(),
                contentDescription = appTitle,
                modifier = Modifier
                    .size(30.sdp)
                    .clip(RoundedCornerShape(5.dp))
                    .shadow(elevation = 0.5.dp)
            )
            HorizontalSpacer(10)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = appTitle,
                style = style,
                textAlign = TextAlign.Start,
                color = textColor
            )
            HorizontalSpacer(10)
            Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
                CustomGradientSwitch(
                    checked = switchChecked,
                    switchEnabled = switchEnabled,
                    onCheckedChange = {
                        onSwitchChecked.invoke(it)
                    })
            }
        }
    }
}