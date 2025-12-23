package com.example.budgetly.ui.cash.home.screens.pie_chart.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import com.example.budgetly.ui.cash.home.state.PieSlice
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.TypoH7
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.textColor
import kotlin.math.atan2

@Composable
fun DonutPieChart(
    modifier: Modifier = Modifier,
    slices: List<PieSlice>,
    emptyLabel:String,
    strokeWidth: Float = 40f,
    selectedStrokeWidth: Float = 50f,
) {
    var selectedSlice by remember { mutableStateOf(slices.firstOrNull()) }
    LaunchedEffect(slices) {
        selectedSlice = slices.firstOrNull()
    }
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged { layoutSize = it }
            .pointerInput(layoutSize, slices) {
                detectTapGestures { offset ->
                    if (slices.isNotEmpty()) {
                        val size = Size(layoutSize.width.toFloat(), layoutSize.height.toFloat())
                        val touchAngle = getTouchAngle(offset, size)
                        selectedSlice = findSliceAtAngle(slices, touchAngle)
                    }
                }
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            if (slices.isEmpty()) {
                drawCircle(color = secondaryBgColor)
                drawArc(
                    color = itemBgColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
            } else {
                var startAngle = -90f
                slices.forEach { slice ->
                    val sweep = slice.proportion * 360f
                    drawArc(
                        color = slice.color.copy(
                            alpha = if (slice == selectedSlice) 0.3f else 0.1f
                        ),
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = true
                    )
                    startAngle += sweep
                }
                startAngle = -90f
                slices.forEach { slice ->
                    val sweep = slice.proportion * 360f
                    drawArc(
                        color = slice.color.copy(
                            alpha = if (slice == selectedSlice) 0.6f else 0.4f
                        ),
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(
                            width = if (slice == selectedSlice) selectedStrokeWidth else strokeWidth,
                            cap = StrokeCap.Butt
                        )
                    )
                    startAngle += sweep
                }
            }
        }

        // Center label
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (slices.isEmpty()) {
                Text(
                    textAlign = TextAlign.Center,
                    text = emptyLabel,
                    style = SubtitleLarge,
                    color = secondaryColor
                )
                VerticalSpacer(8)
                Text(
                    textAlign = TextAlign.Center,
                    text = "0%",
                    style = TypoH7,
                    color = primaryColor
                )
                VerticalSpacer(8)
                Text(
                    textAlign = TextAlign.Center,
                    text = "0.00",
                    style = TypoH7,
                    color = textColor
                )
            } else {
                selectedSlice?.let { sel ->
                    Text(
                        textAlign = TextAlign.Center,
                        text = sel.label,
                        style = SubtitleLarge,
                        color = textColor
                    )
                    VerticalSpacer(8)
                    Text(
                        textAlign = TextAlign.Center,
                        text = "${(sel.proportion * 100).toInt()}%",
                        style = TypoH7,
                        color = primaryColor
                    )
                    VerticalSpacer(8)
                    Text(
                        textAlign = TextAlign.Center,
                        text = String.format("%.2f", sel.amount),
                        style = TypoH7,
                        color = sel.color
                    )
                }
            }
        }
    }
}



/*@Composable
fun DonutPieChart(
    modifier: Modifier = Modifier,
    slices: List<PieSlice>,
    strokeWidth: Float = 40f,
    selectedStrokeWidth: Float = 50f,
) {
    var selectedSlice by remember { mutableStateOf(slices.firstOrNull()) }
    LaunchedEffect(slices){
        selectedSlice = slices.first()
    }
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged { layoutSize = it }
            .pointerInput(layoutSize, slices) {
                detectTapGestures { offset ->
                    val size = Size(layoutSize.width.toFloat(), layoutSize.height.toFloat())
                    val touchAngle = getTouchAngle(offset, size)
                    selectedSlice = findSliceAtAngle(slices, touchAngle)
                }
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            var startAngle = -90f

            // 1️⃣ Fill slices as pieces of cake (solid) with low opacity
            slices.forEach { slice ->
                val sweep = slice.proportion * 360f
                drawArc(
                    color = slice.color.copy(
                        alpha = if (slice == selectedSlice) 0.3f else 0.1f
                    ),
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true // <- draw full filled arc
                )
                startAngle += sweep
            }

            // 2️⃣ Draw all slice borders at 0.4 opacity, EXCEPT the selected one
            startAngle = -90f
            slices.forEach { slice ->
                if (slice == selectedSlice) {
                    startAngle += slice.proportion * 360f
                    return@forEach // skip drawing border for selected
                }

                val sweep = slice.proportion * 360f
                drawArc(
                    color = slice.color.copy(alpha = 0.4f),
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
                startAngle += sweep
            }

            // 3️⃣ Highlight selected slice border at 0.6 opacity
            selectedSlice?.let { sel ->
                val selStartAngle = slices
                    .takeWhile { it != sel }
                    .sumOf { it.proportion.toDouble() }
                    .toFloat() * 360f - 90f
                drawArc(
                    color = sel.color.copy(alpha = 0.6f),
                    startAngle = selStartAngle,
                    sweepAngle = sel.proportion * 360f,
                    useCenter = false,
                    style = Stroke(width = selectedStrokeWidth, cap = StrokeCap.Butt)
                )
            }
        }

        // 4️⃣ Label in center
        selectedSlice?.let { sel ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    textAlign = TextAlign.Center,
                    text = sel.label,
                    style = SubtitleLarge,
                    color = textColor
                )
                VerticalSpacer(8)
                Text(
                    textAlign = TextAlign.Center,
                    text = "${(sel.proportion * 100).toInt()}%",
                    style = TypoH7,
                    color = primaryColor
                )
                VerticalSpacer(8)
                Text(
                    textAlign = TextAlign.Center,
                    text = String.format("%.2f", sel.amount),
                    style = TypoH7,
                    color = sel.color
                )
            }
        }
    }
}*/



/*@Composable
fun DonutPieChart(
    modifier: Modifier = Modifier,
    slices: List<PieSlice>,
    strokeWidth: Float = 40f,
    selectedStrokeWidth: Float = 50f,
) {
    val nonZeroSlices = slices.filter { it.proportion > 0f }
    val selectedSlice = remember { mutableStateOf(nonZeroSlices.firstOrNull()) }
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged { layoutSize = it }
            .pointerInput(layoutSize, nonZeroSlices) {
                detectTapGestures { offset ->
                    val size = Size(layoutSize.width.toFloat(), layoutSize.height.toFloat())
                    val touchAngle = getTouchAngle(offset, size)
                    selectedSlice.value = findSliceAtAngle(nonZeroSlices, touchAngle)
                }
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            var startAngle = -90f

            // Draw all slices with low opacity as background
            nonZeroSlices.forEach { slice ->
                val sweep = slice.proportion * 360f
                drawArc(
                    color = slice.color.copy(alpha = 0.3f),
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
                startAngle += sweep
            }

            // Draw selected slice on top
            startAngle = -90f
            nonZeroSlices.forEach { slice ->
                val sweep = slice.proportion * 360f
                if (slice == selectedSlice.value) {
                    drawArc(
                        color = slice.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = selectedStrokeWidth, cap = StrokeCap.Butt)
                    )
                }
                startAngle += sweep
            }
        }

        // Draw label in center
        selectedSlice.value?.let { sel ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = sel.label,
                    style = SubtitleLarge,
                    color = textColor
                )
                VerticalSpacer(8)
                Text(
                    text = "${(sel.proportion * 100).toInt()}%",
                    style = TypoH7,
                    color = primaryColor
                )
                VerticalSpacer(8)
                Text(
                    text = String.format("%.2f", sel.amount),
                    style = TypoH7,
                    color = sel.color
                )
            }
        }
    }
}*/
private fun getTouchAngle(offset: Offset, size: Size): Float {
    val dx = offset.x - size.width / 2
    val dy = offset.y - size.height / 2
    val angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
    return (angle + 360f + 90f) % 360f // +90 because 0 is at top
}

private fun findSliceAtAngle(slices: List<PieSlice>, angle: Float): PieSlice? {
    var start = 0f
    slices.forEach { slice ->
        val sweep = slice.proportion * 360f
        if (angle in start..(start + sweep)) {
            return slice
        }
        start += sweep
    }
    return null
}


