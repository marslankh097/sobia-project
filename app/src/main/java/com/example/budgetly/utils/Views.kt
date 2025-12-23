package com.example.budgetly.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ir.kaaveh.sdpcompose.sdp

@Composable
fun HorizontalSpacer(size: Int = 10) {
    Spacer(modifier = Modifier.width(size.dp))
}
@Composable
fun VerticalSpacer(size: Int = 10) {
    Spacer(modifier = Modifier.height(size.dp))
}

@Composable
fun HorizontalSpacerDp(size: Int = 10) {
    Spacer(modifier = Modifier.width(size.dp))
}

@Composable
fun VerticalSpacerDp(size: Int = 10) {
    Spacer(modifier = Modifier.height(size.dp))
}
 @Composable
 fun  ThreeDots(size:Int = 3, color:Color = primaryColor, space:Int = 5){
     Dot(size, color)
     HorizontalSpacer(space)
     Dot(size,color)
     HorizontalSpacer(space)
     Dot(size, color)
     HorizontalSpacer(space)
 }
@Composable
fun Dot(size: Int =3, color: Color = primaryColor){
    Box(modifier = Modifier.size(size.sdp).clip(CircleShape).background(color))
}
@Composable
fun TextWithUnderLine(modifier: Modifier, text:String, color:Color, style: TextStyle){
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = text, color = color, style = style)
        HorizontalDivider(color = color, thickness = 1.sdp, modifier = Modifier.width(60.sdp).padding(horizontal = 12.sdp))
    }
}
val PrimaryGradient = listOf(primaryColor,primaryColor)
val SecondaryGradient = listOf(secondaryColor,secondaryColor)
var lastClickTime = 0L
fun Modifier.combinedClick(
    debounceTime: Long = 400L,
    longPressTime: Long = 500L,
    onClick: () -> Unit,
    onLongClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(0L) }

    pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                val down = awaitFirstDown(requireUnconsumed = false)
                val touchSlop = viewConfiguration.touchSlop
                var isDragging = false
                var longPressed = false

                val longPressResult = withTimeoutOrNull(longPressTime) {
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.first()

                        if (change.positionChange().getDistance() > touchSlop) {
                            isDragging = true
                            break
                        }

                        if (change.changedToUpIgnoreConsumed()) {
                            break
                        }
                    }
                }

                longPressed = longPressResult == null

                if (longPressed) {
                    onLongClick()
                } else if (!isDragging) {
                    val currentTime = System.currentTimeMillis()
                    if ((currentTime - lastClickTime) >= debounceTime) {
                        lastClickTime = currentTime
                        onClick()
                    }
                }
            }
        }
    }
}
fun Modifier.safeClickAble(time:Long=400, onClick: () -> Unit): Modifier = composed {
    this.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) < time) return@clickable
        lastClickTime = currentTime
        onClick.invoke()
    }
}