package com.example.budgetly.ui.cash.home.screens.pie_chart.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.budgetly.utils.LabelLarge
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.greyShade2
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun PieChartProgressBar(
    modifier: Modifier = Modifier,
    percentage: Double,
    labelColor: Color = textColor,
    progressColor: Color = primaryColor,
    trackColor: Color = greyShade2,
) {
    val progressFraction =  (percentage / 100f).coerceIn(0.0, 1.0).toFloat()
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
    ) {
        // Outer track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.sdp)
                .clip(CircleShape) // round all
                .background(trackColor)
        ) {
            // Progress fill
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressFraction) // scale by progress
                    .height(4.sdp)
                    .clip(CircleShape) // round ends
                    .background(progressColor).align(Alignment.CenterStart)
            )
        }

        VerticalSpacer(4)

        Text(
            text = String.format("%.2f%% of total", percentage),
            style = LabelLarge,
            color = labelColor
        )
    }
}



/*@Composable
fun PieChartProgressBar(
    modifier: Modifier = Modifier,
    percentage: Double,
    labelColor: Color = textColor,
    progressColor: Color = primaryColor,
    trackColor: Color = greyShade1,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        LinearProgressIndicator(
            progress = { (percentage / 100f).coerceIn(0.0, 1.0).toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.sdp),
//                .clip(CircleShape),
            color = progressColor,
            trackColor = trackColor
        )
        VerticalSpacer(4)
        Text(
            text = String.format("%.2f%% of total", percentage),
            style = LabelMedium,
            color = labelColor
        )
    }
}*/

