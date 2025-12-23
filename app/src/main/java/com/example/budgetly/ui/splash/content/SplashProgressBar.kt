package com.example.budgetly.ui.splash.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun  SplashProgressBar(modifier: Modifier = Modifier, textId:Int, progress:Float){
    Column(modifier = modifier.fillMaxWidth()){
        Text(
            text = "${(progress * 100f).toInt()} %",
            modifier = Modifier.fillMaxWidth().padding(end = 20.sdp),
            color = textColor,
            style = SubtitleLarge,
            textAlign = TextAlign.End,
        )
        VerticalSpacer(10)
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.sdp)
                .background(
                    brush = Brush.linearGradient(listOf(primaryColor, primaryColor)),
                    shape = CircleShape
                )
                .padding(vertical = 2.sdp, horizontal = 3.sdp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.sdp),
                color = white,
                trackColor = Color.Transparent,
//                strokeCap = StrokeCap.Round,
            )
        }
        VerticalSpacer(20)
        Text(
            text = stringResource(id = textId),
            modifier = Modifier.fillMaxWidth(),
            color = hintColor,
            style = SubtitleLarge,
            textAlign = TextAlign.Center,
        )
    }
}