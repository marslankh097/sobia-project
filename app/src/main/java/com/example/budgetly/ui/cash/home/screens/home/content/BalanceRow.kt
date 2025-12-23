package com.example.budgetly.ui.cash.home.screens.home.content
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.TypoH7
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun BalanceRow(
    balance: String,
    currency: String,
    modifier: Modifier = Modifier,
    label: String = "Account Balance:",
    labelStyle: TextStyle = SubtitleLarge,
    labelColor: Color = secondaryColor,
    currencyStyle: TextStyle = TypoH7,
    currencyColor: Color = primaryColor,
    balanceColor: Color = textColor
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.sdp), verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = label,
            style = labelStyle,
            color = labelColor
        )
        VerticalSpacer(12)
        Row {
            Text(
                text = currency,
                style = currencyStyle,
                color = currencyColor
            )
            HorizontalSpacer()
            Text(
                text = balance,
                style = currencyStyle,
                color = balanceColor,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
