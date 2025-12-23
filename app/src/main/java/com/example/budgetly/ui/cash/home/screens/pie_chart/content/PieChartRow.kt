package com.example.budgetly.ui.cash.home.screens.pie_chart.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.budgetly.utils.BodyMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun PieChartRow(
    modifier: Modifier = Modifier,
    categoryName: String,
    amount: Double,
    percentage: Double,
    currency: String,
    onClick:()->Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.sdp).safeClickAble {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryName,
                style = BodyMedium,
                color = primaryColor
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = String.format("%.2f", amount),
                    style = SubtitleLarge,
                    color = secondaryColor
                )
                HorizontalSpacer(4)
                Text(
                    text = currency,
                    style = SubtitleLarge,
                    color = primaryColor
                )
            }
        }

        VerticalSpacer(8)
        PieChartProgressBar(percentage = percentage)
    }
}

