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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.combinedClick
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.HighlightedText
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SubCategoryRow(
    modifier: Modifier = Modifier,
    iconId:Int,
    title: String,
    subTitle: String = "",
    currency:String = "",
    searchText:String,
    style: TextStyle = SubtitleSmall,
    onSubCategorySelected: () -> Unit,
    onLongPressed:()->Unit = {}
) {
    RoundedBorderRectangle(
        modifier = modifier
            .combinedClick(
            onClick = { onSubCategorySelected.invoke() },
            onLongClick = { onLongPressed() }
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
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    HighlightedText(
                        Modifier.fillMaxWidth().weight(1f),
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
//                Text(
//                    text = title.toSafeEmptyString(),
//                    style = style,
//                    textAlign = TextAlign.Start,
//                    color = textColor
//                )
            }
            HorizontalSpacer(10)
        }
    }
}