package com.example.budgetly.ui.cash.category.screens.category_display.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.demo.budgetly.R
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.combinedClick
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.log
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.HighlightedText
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CategoryRow(
    modifier: Modifier = Modifier,
    iconId:Int,
    title: String,
    subTitle: String = "",
    currency:String = "",
    searchText:String,
    isExpanded:Boolean,
    style: TextStyle = SubtitleSmall,
    isExpandable:Boolean = true,
    onCategorySelected: () -> Unit,
    onCategoryExpanded: () -> Unit = {},
    onLongPressed:()->Unit = {}
) {
    var expandClicked by remember { mutableStateOf(false) }
    RoundedBorderRectangle(
        modifier = modifier
            .combinedClick(
            onClick = {
                if(expandClicked){
                    expandClicked = false
                    return@combinedClick
                }
                log("onClick:isExpanded: ${isExpanded}")
                onCategorySelected.invoke() },
            onLongClick = {
                log("onLongClick:isExpanded:${isExpanded}")
                onLongPressed()
            }
        ),
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Row(modifier = modifier.padding(10.sdp), verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier.size(30.sdp), painter = painterResource(iconId), contentDescription = title)
            HorizontalSpacer(10)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HighlightedText(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        fullText = title.toSafeEmptyString(),
                        style = style,
                        textColor = textColor,
                        searchText = searchText
                    )
                    Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.End){
                        if(currency.isNotEmpty()){
                            HorizontalSpacer(15)
                            Text(
                                text =  currency,
                                style = SubtitleMedium,
                                color = secondaryColor
                            )
                        }
                        if(subTitle.isNotEmpty()){
                            HorizontalSpacer()
                            Text(
                                text =  subTitle,
                                style = SubtitleMedium,
                                color = primaryColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
            HorizontalSpacer(10)
            Row(modifier = Modifier.safeClickAble {
                     expandClicked = true
                    onCategoryExpanded() }, horizontalArrangement = Arrangement.End) {
                    if(isExpandable){
                    Image(modifier = Modifier.size(32.sdp).padding(13.sdp),
                        painter = painterResource(if (isExpanded) R.drawable.icon_collapse else R.drawable.icon_expand),
                        contentDescription = title,
                        colorFilter = ColorFilter.tint(secondaryColor)
                    )
                    }else if(isExpanded){
                        Image(modifier = Modifier.size(20.sdp),
                            painter = painterResource(R.drawable.icon_selected),
                            contentDescription = title
                        )
                        HorizontalSpacer(12)
                    }
                }
        }
    }
}