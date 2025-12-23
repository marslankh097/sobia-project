package com.example.budgetly.ui.cash.sort.content

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.budgetly.utils.BodySmall
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.inactiveButtonColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.CoilImage
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SortOptionItem(
    @DrawableRes icon: Int,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.sdp)
            .safeClickAble { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.size(35.sdp).background(shape = CircleShape,
            color =if (isSelected) secondaryBgColor else inactiveButtonColor), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            CoilImage(
                model = icon,
                placeholder = icon,
                modifier = Modifier.size(28.sdp).padding(4.sdp),
                contentScale = ContentScale.Fit,
                applyTint =  true,
                bgColor = if (isSelected) secondaryBgColor else inactiveButtonColor,
                tintColor = if (isSelected) primaryColor else hintColor,
                showBackground = true
            )
        }
        VerticalSpacer(8)
        Text(
            text = text,
            color = if (isSelected) primaryColor else textColor,
            style = BodySmall
        )
    }
}
