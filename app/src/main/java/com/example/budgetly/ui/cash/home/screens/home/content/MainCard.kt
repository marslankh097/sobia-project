package com.example.budgetly.ui.cash.home.screens.home.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun MainCard(modifier: Modifier = Modifier,
             imageId:Int,
             title:String,
             amount:String,
             currency:String,
             subTitle:String,
             imageSize:Int = 50,
             titleStyle: TextStyle = SubtitleLarge,
             subTitleStyle: TextStyle = SubtitleSmall,
             onCardClick:()->Unit){
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble {
            onCardClick.invoke()
        },
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Row(modifier = modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier.size(imageSize.sdp), painter = painterResource(imageId), contentDescription = title)
            HorizontalSpacer(15)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title.toSafeEmptyString(),
                        style = titleStyle,
                        textAlign = TextAlign.Start,
                        color = textColor
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (currency.isNotEmpty()) {
                            HorizontalSpacer(15)
                            Text(
                                text = currency,
                                style = SubtitleMedium,
                                color = secondaryColor
                            )
                        }
                        if (amount.isNotEmpty()) {
                            HorizontalSpacer()
                            Text(
                                text = amount,
                                style = SubtitleMedium,
                                color = primaryColor
                            )
                        }
                    }
                }
                VerticalSpacer(8)
                Text(
                    text = subTitle.toSafeEmptyString(),
                    style = subTitleStyle,
                    textAlign = TextAlign.Start,
                    color = hintColor
                )
            }
            HorizontalSpacer(10)
        }
    }

}