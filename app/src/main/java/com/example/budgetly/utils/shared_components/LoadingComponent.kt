package com.example.budgetly.utils.shared_components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.secondaryColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun LoadingComponent(modifier: Modifier = Modifier,textId: Int) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.sdp), color = secondaryColor)
        VerticalSpacer(8)
        Text(
            modifier = Modifier.fillMaxWidth(),
            color = hintColor,
            textAlign = TextAlign.Center,
            text = stringResource(textId)
        )
    }
}