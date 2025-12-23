package com.example.budgetly.ui.notification_listener.screen.content

import android.graphics.drawable.Drawable
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
import com.example.budgetly.utils.shared_components.CoilImage
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.shared_components.SmallRoundedButton
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun NotificationRow(
    modifier: Modifier = Modifier,
    icon:Drawable?,
    placeholder: Int = R.drawable.icon_notification,
    title: String,
    subTitle: String,
    msg: String,
    isExpanded:Boolean = false,
    appName:String,
    style: TextStyle = SubtitleSmall,
    buttonText:String = stringResource(R.string.add),
    onNotificationSelected: () -> Unit,
    onDeleteClicked:()->Unit,
    onButtonClick:()->Unit
) {
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble {
            onNotificationSelected.invoke()
        },
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Column (modifier = modifier.padding(12.sdp)){
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CoilImage(modifier = Modifier.size(30.sdp), model = icon ?: placeholder, placeholder = placeholder)
                    if(appName.isNotEmpty()){
                        VerticalSpacer(5)
                        Text(
                            text = appName,
                            style = SubtitleMedium,
                            textAlign = TextAlign.Start,
                            color = secondaryColor
                        )
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
                    if(isExpanded) {
                        VerticalSpacer(17)
                        Row(horizontalArrangement = Arrangement.Center) {
                            Text(
                                text = stringResource(R.string.message),
                                style = ButtonMedium,
                                textAlign = TextAlign.Start,
                                color = textColor
                            )
                            HorizontalSpacer(8)
                            Text(
                                text = msg,
                                style = SubtitleMedium,
                                textAlign = TextAlign.Center,
                                color = textColor
                            )
                        }
                    }
                }
                Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
                    Image(modifier = Modifier
                        .size(12.sdp)
                        .padding(2.sdp),
                        painter = painterResource(if(isExpanded) R.drawable.icon_collapse else R.drawable.icon_expand),
                        contentDescription = title,
                        colorFilter = ColorFilter.tint(secondaryColor)
                    )

                }
                HorizontalSpacer(10)
            }
            if(isExpanded){
                VerticalSpacer(20)
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    SmallRoundedButton(false,
                        stringResource(R.string.discard), onButtonClick = {onDeleteClicked()})
                    HorizontalSpacer()
                    SmallRoundedButton(true, buttonText, onButtonClick = {onButtonClick()})
                }
            }
        }
    }
}