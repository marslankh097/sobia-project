package com.example.budgetly.utils.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RoundedBorderRectangle(
    modifier: Modifier = Modifier,
    bgColor: Color,
    borderColor: Color,
    borderRadius: Float,
    borderThickness: Float,
    elevation:Int= 0,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(borderRadius.dp))
            .shadow(elevation = elevation.dp,shape = RoundedCornerShape(borderRadius.dp))
            .border(
                width = borderThickness.dp,
                color = borderColor,
                shape = RoundedCornerShape(borderRadius.dp)
            )
    ) {
        content()
    }
}

@Composable
fun GradientBorderRectangle(
    modifier: Modifier = Modifier,
    bgBrush: Brush,
    borderBrush: Brush,
    borderRadius: Float,
    borderThickness: Float,
    elevation:Int= 0,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(brush = bgBrush, shape = RoundedCornerShape(borderRadius.dp))
            .shadow(elevation = elevation.dp,shape = RoundedCornerShape(borderRadius.dp))
            .border(
                width = borderThickness.dp,
                brush = borderBrush,
                shape = RoundedCornerShape(borderRadius.dp)
            )
    ) {
        content()
    }
}
