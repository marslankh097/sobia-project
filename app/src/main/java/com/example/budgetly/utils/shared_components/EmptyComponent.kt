package com.example.budgetly.utils.shared_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun EmptyComponent(modifier: Modifier = Modifier, image: Int, text: Int) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "",
            alignment = Alignment.Center,
            modifier = Modifier.size(50.sdp)
        )
        VerticalSpacer(5)
        Text(
            text = stringResource(id = text),
            textAlign = TextAlign.Center, color = hintColor
        )
    }
}