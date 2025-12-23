package com.example.budgetly.ui.notification_listener.screen.content

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import com.demo.budgetly.R
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.ButtonSmall
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.btnGradientColor1
import com.example.budgetly.utils.getRawResourceString
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.GifImage
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.SwitchRow
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun PermissionComponent(
    modifier: Modifier = Modifier,
    context: Context,
    title: String,
    msg: String = "",
    icon: Int,
    buttonText: String = stringResource(R.string.enable),
    showSwitchRow:Boolean = false,
    onButtonClick: () -> Unit
) {
    val appIcon = ContextCompat.getDrawable(context,R.drawable.img_bank)
    val gifUri = "android.resource://${context.packageName}/${R.raw.gif_notification_permission}"
    Column(
        modifier = modifier.background(itemBgColor).padding(12.sdp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.sdp),
            text = title,
            style = ButtonSmall,
            color = textColor,
            textAlign = TextAlign.Center
        )
        VerticalSpacer(10)
        GifImage(modifier = Modifier.weight(1f), gifUri = context.getRawResourceString(R.raw.gif_notification_permission)
            ,placeholder = R.drawable.img_notification_permission)
        if(msg.isNotEmpty()){
            VerticalSpacer(10)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.sdp),
                text = msg,
                color = secondaryColor,
                style = SubtitleSmall,
                textAlign = TextAlign.Center
            )
        }
        if(showSwitchRow){
            VerticalSpacer(10)
            if(appIcon != null){
                SwitchRow(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.sdp),
                    appIcon = appIcon,
                    paddingValues = PaddingValues(vertical = 16.sdp, horizontal = 12.sdp),
                    appTitle = stringResource(id = R.string.app_name),
                    switchEnabled = false,
                    switchChecked = true,
                    onSwitchChecked = {},
                    borderColors = listOf(
                        btnGradientColor1, btnGradientColor1
                    )
                )
            }
        }
        VerticalSpacer(20)
        PrimaryButtonRounded(
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(secondaryColor, secondaryColor),
            onButtonClick = onButtonClick
        ) {
            Text(text = buttonText, style = ButtonMedium, color = Color.White)
        }
    }
}