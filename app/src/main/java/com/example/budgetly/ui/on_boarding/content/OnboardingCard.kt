package com.example.budgetly.ui.on_boarding.content

import androidx.compose.foundation.Image
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
import com.example.budgetly.utils.BodyMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun OnBoardingCard(modifier: Modifier = Modifier,
                   imgId:Int, title:String,
                   subTitle:String,
                   imgSize:Int = 50,
                   titleStyle:TextStyle = SubtitleLarge,
                   subTitleStyle:TextStyle = BodyMedium,
                   onCardClick:()->Unit){
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble {
            onCardClick.invoke()
        },
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 12f, borderThickness = 1f
    ) {
        Row(modifier = modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier.size(imgSize.sdp), painter = painterResource(imgId), contentDescription = title)
            HorizontalSpacer(15)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = title.toSafeEmptyString(),
                    style = titleStyle,
                    textAlign = TextAlign.Start,
                    color = textColor
                )
                VerticalSpacer(8)
                Text(
                    text = subTitle.toSafeEmptyString(),
                    style = subTitleStyle,
                    textAlign = TextAlign.Start,
                    color = hintColor
                )
            }
            HorizontalSpacer(12)
        }
    }
}