package com.example.budgetly.ui.cash.home.screens.home.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    currency: String,
    subTitle: String,
    bgColor: Color = itemBgColor,
    onCardClick: () -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier
            .fillMaxWidth()
            .safeClickAble {
                onCardClick.invoke()
            },
        bgColor = bgColor,
        borderColor = greyShade1,
        borderRadius = 8f,
        borderThickness = 1f
    ) {
        Column(
            modifier = modifier
                .height(104.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(30.sdp)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End){
                    if(currency.isNotEmpty()){
                        HorizontalSpacer(15)
                        Text(
                            text =  currency,
                            style = SubtitleMedium,
                            color = secondaryColor
                        )
                    }
                    if(subTitle.isNotEmpty()){
                        HorizontalSpacer()
                        Text(
                            text =  subTitle,
                            style = SubtitleMedium,
                            color = primaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = SubtitleMedium,
                    color = textColor,
                    maxLines = 3
                )
            }
        }
    }
}