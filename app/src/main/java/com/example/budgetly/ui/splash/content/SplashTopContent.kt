 package com.example.budgetly.ui.splash.content
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.demo.budgetly.R
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.TypoH5
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.shared_components.ComposeLottieAnimation

 @Composable
fun SplashTopContent(modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        ComposeLottieAnimation(
            modifier = Modifier.fillMaxWidth().weight(1f)
                .align(Alignment.CenterHorizontally),
            lottie = R.raw.splash_animation,
        )
        VerticalSpacer(20)
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "AI Budgetly App",
            textAlign = TextAlign.Center,
            style = TypoH5, color = primaryColor
        )
        VerticalSpacer(20)
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Let AI Empower Your Financial Journey", style = SubtitleLarge, color = hintColor )
    }
}