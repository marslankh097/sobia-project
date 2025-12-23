package com.example.budgetly.ui.cash.filter.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.LabelSmall
import com.example.budgetly.utils.inactiveButtonColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun FilterOption(modifier: Modifier = Modifier, title:String, isSelected:Boolean,
                 onFilterOptionClick:(Boolean)->Unit){
    Column(modifier = modifier.background(shape = RoundedCornerShape(50.sdp), color =if (isSelected) secondaryBgColor else inactiveButtonColor
    ).safeClickAble {
        onFilterOptionClick(!isSelected)
    },verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            style = LabelSmall,
            color = white,
            text = title
        )
    }
}