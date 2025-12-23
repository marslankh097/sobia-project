package com.example.budgetly.utils.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.demo.budgetly.R
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun FailureComponent(
    modifier: Modifier = Modifier,
    msg: String,
    showBtn:Boolean = true,
    tryAgainText: String = stringResource(R.string.try_again),
    onTryAgainClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.sdp),
            text = msg,
            color = hintColor,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        if(showBtn){
            VerticalSpacer(10)
            Box(modifier = Modifier.background(color = secondaryColor,
                CircleShape).padding(horizontal = 20.sdp, vertical = 10.sdp)
                .safeClickAble {
                    onTryAgainClick()
                }
            ) {
                Text(text = tryAgainText, style = ButtonMedium, color = Color.White)
            }
        }
    }
}