package com.example.budgetly.ui.assistant.screens.chat_history.content

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.demo.budgetly.R
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun AssistantHistoryRow(
    modifier: Modifier = Modifier,
    iconId:Int,
    title: String,
    subTitle: String,
    chatCount:String,
    style: TextStyle = SubtitleSmall,
    onTransactionSelected: () -> Unit,
    onDeleteClicked:()->Unit
) {
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble {
            onTransactionSelected.invoke()
        },
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Row(modifier = modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(25.sdp),
                    painter = painterResource(iconId),
                    contentDescription = title
                )
                if(chatCount.isNotEmpty()){
                    VerticalSpacer(5)
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.count),
                            style = ButtonMedium,
                            textAlign = TextAlign.Start,
                            color = textColor
                        )
                        Text(
                            text = chatCount,
                            style = SubtitleMedium,
                            textAlign = TextAlign.Start,
                            color = secondaryColor
                        )
                    }
                }
            }
            HorizontalSpacer()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), verticalArrangement = Arrangement.Center
            ){
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = stringResource(R.string.title ),
                        style = ButtonMedium,
                        textAlign = TextAlign.Start,
                        color = textColor
                    )
                    HorizontalSpacer(8)
                    Text(
                        text = title,
                        style = style,
                        textAlign = TextAlign.Start,
                        color = primaryColor
                    )
                }
                VerticalSpacer(17)
                Row ( verticalAlignment = Alignment.Bottom){
                    Text(
                        text = stringResource(R.string.date),
                        style = ButtonMedium,
                        textAlign = TextAlign.Start,
                        color = textColor
                    )
                    HorizontalSpacer(8)
                    Text(
                        text = subTitle,
                        style = style,
                        textAlign = TextAlign.Start,
                        color = secondaryColor
                    )
                }
            }
            HorizontalSpacer(10)
            Row(modifier = Modifier.safeClickAble {
                onDeleteClicked() }, horizontalArrangement = Arrangement.End) {
                Image(modifier = Modifier.size(20.sdp).padding(2.sdp),
                    painter = painterResource(R.drawable.icon_delete),
                    contentDescription = title,
                    colorFilter = ColorFilter.tint(secondaryColor)
                )

            }
        }
    }
}