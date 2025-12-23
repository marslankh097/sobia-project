package com.example.budgetly.ui.cash.transaction.screens.transaction_display.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.TypoH7
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.combinedClick
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun TransactionRow(
    modifier: Modifier = Modifier,
    iconId: Int,
    iconSize:Int = 40,
    maxHeight:Int = 80,
    spacer:Int = 10,
    currency: String,
    amount: String,
    frequency: String,
    subCategory: String,
    subCategoryTitle: String = "SubCategory:" ,
    dateTitle: String ="Date:",
    headingStyle:TextStyle = TypoH7,
    date: String,
    onTransactionSelected: () -> Unit,
    onLongPressed: () -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier
            .fillMaxWidth()
            .height(maxHeight.sdp) // Optional fixed height for even distribution
            .combinedClick(
                onClick = {
                    onTransactionSelected()
            }, onLongClick = {
                    onLongPressed()
            }),
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f,
        borderThickness = 1f
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.sdp, vertical = 8.sdp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier.size(iconSize.sdp),
                    painter = painterResource(iconId),
                    contentDescription = amount
                )
                if(frequency.isNotEmpty()){
                    VerticalSpacer(spacer)
                    Text(
                        text = frequency,
                        style = SubtitleLarge,
                        textAlign = TextAlign.Start,
                        color = secondaryColor
                    )
                }
            }

            HorizontalSpacer(15)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)

            ) {
                // Amount Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {
                    if(currency.isNotEmpty()){
                        Text(
                            text = currency,
                            style = headingStyle,
                            color = primaryColor
                        )
                        HorizontalSpacer(12)
                    }
                    Text(
                        text = amount,
                        style = headingStyle,
                        color = secondaryColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Subcategory Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subCategoryTitle,
                        style = ButtonMedium,
                        color = textColor
                    )
                    HorizontalSpacer()
                    Text(
                        text = subCategory,
                        style = SubtitleMedium,
                        color = primaryColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Date Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateTitle,
                        style = ButtonMedium,
                        color = textColor
                    )
                    HorizontalSpacer()
                    Text(
                        text = date,
                        style = SubtitleMedium,
                        color = primaryColor
                    )
                }
            }

            HorizontalSpacer(10)
        }
    }
}

