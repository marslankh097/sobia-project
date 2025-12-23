package com.example.budgetly.ui.cash.category.screens.category_insert.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.demo.budgetly.R
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CustomSelectionRow(
    modifier: Modifier = Modifier,
    title: String,
    currency: String = "",
    isExpanded:Boolean,
    bgColor:Color = itemBgColor,
    txtColor:Color = textColor,
    tintColor:Color = secondaryColor,
    style: TextStyle = SubtitleLarge,
    isExpandable:Boolean = true,
    onCategorySelected: (Boolean) -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble { onCategorySelected.invoke(if(isExpandable) isExpanded.not() else true) },
        bgColor = bgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Row(modifier = modifier.fillMaxWidth().padding(10.sdp), verticalAlignment = Alignment.CenterVertically) {
            HorizontalSpacer(10)
            Row(modifier = Modifier.fillMaxWidth().weight(1f)){
                if(currency.isNotEmpty()){
                    Text(
                        text =  currency,
                        style = SubtitleMedium,
                        color = primaryColor
                    )
                    HorizontalSpacer()
                }
                if(title.isNotEmpty()){
                    Text(
                        text = title.toSafeEmptyString(),
                        style = style,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = txtColor
                    )
                }
            }
//            Column(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) {
//
//            }
            HorizontalSpacer(10)
            Row( horizontalArrangement = Arrangement.End) {
                    if(isExpandable){
                    Image(modifier = Modifier.size(7.sdp),
                        painter = painterResource(if (isExpanded) R.drawable.icon_collapse else R.drawable.icon_expand),
                        contentDescription = title,
                        colorFilter = ColorFilter.tint(tintColor)
                    )
                    }else if(isExpanded){
                        Image(modifier = Modifier.size(20.sdp),
                            painter = painterResource(R.drawable.icon_selected),
                            contentDescription = title
                        )
                    }
                }
            HorizontalSpacer(12)
        }
    }
}