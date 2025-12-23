package com.example.budgetly.utils.shared_components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryColor

@Composable
fun CustomGradientSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    switchEnabled: Boolean = true,
    thumbColor: Color = Color.White,
    trackCheckedGradient: Brush = Brush.horizontalGradient(
        colors = listOf(primaryColor, secondaryColor) // Gradient when checked
    ),
    trackUncheckedGradient: Brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFACACAC), Color(0xFF757575)) // Neutral gradient when unchecked
    )
) {
    val thumbPosition by animateDpAsState(
        targetValue = if (checked) 22.dp else 2.dp,
        label = "ThumbPositionAnimation"
    )

    Box(
        modifier = modifier
            .size(width = 40.dp, height = 20.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (checked) trackCheckedGradient else trackUncheckedGradient)
            .clickable { if (switchEnabled) onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .offset(x = thumbPosition)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}