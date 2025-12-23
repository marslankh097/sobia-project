package com.example.budgetly.utils.shared_components
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.demo.budgetly.R
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.TypoH6
import com.example.budgetly.utils.inactiveButtonColor
import com.example.budgetly.utils.inactiveTextColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp


@Composable
fun TopBar(
    title: String,
    isActivity: Boolean = true,
    icon1: Int? = null,
    icon2: Int? = null,
    icon3: Int? = null,
    showApply:Boolean = false,
    isButtonActive:Boolean = true,
    buttonText:String = stringResource(R.string.done),
    onClickIcon1: (() -> Unit)? = null,
    onClickIcon2: (() -> Unit)? = null,
    onClickIcon3: (() -> Unit)? = null,
    onButtonClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .background(itemBgColor)
            .padding(12.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (isActivity) {
            Image(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = "",
                modifier = Modifier
                    .size(20.sdp)
                    .safeClickAble {
                        onClick?.invoke()
                    }
            )
        }
        Text(
            text = title,
            color = if (isActivity) textColor else primaryColor,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 12.dp),
            textAlign = TextAlign.Start,
            style = if (isActivity) MaterialTheme.typography.titleMedium else  TypoH6
        )
        icon1?.let {
            Image(
                painter = painterResource(id = icon1),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.sdp).padding(4.sdp)
                    .safeClickAble {
                        onClickIcon1?.invoke()
                    }
            )
        }
        icon2?.let {
            Image(
                painter = painterResource(id = icon2),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.sdp).padding(4.sdp)
                    .safeClickAble {
                        onClickIcon2?.invoke()
                    }
            )
        }
        icon3?.let {
            Image(
                painter = painterResource(id = icon3),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.sdp).padding(1.sdp)
                    .safeClickAble {
                        onClickIcon3?.invoke()
                    }
            )
        }
        if(showApply){
            Box(
                modifier = Modifier.width(80.dp)
                    .padding(horizontal = 5.dp)
                    .clip(CircleShape)
                    .background(
                        brush = if(isButtonActive){
                            Brush.linearGradient(listOf(secondaryColor, secondaryColor))
                        }else{
                            Brush.linearGradient(listOf(inactiveButtonColor, inactiveButtonColor))
                        }
                        , CircleShape
                    )
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                    .safeClickAble {
                        onButtonClick?.invoke()
                    }, contentAlignment = Alignment.Center
            ) {
                Text(text = buttonText, style = SubtitleMedium, color = if(isButtonActive) white else inactiveTextColor)
            }
        }
    }
}