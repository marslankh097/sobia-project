package com.example.budgetly.ui.upload_receipt.screens.receipt_result.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.demo.budgetly.R
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.TypoH7
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.shared_components.SmallRoundedButton
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ReceiptResultRow(
    modifier: Modifier = Modifier,
    iconId: Int,
    iconSize:Int = 40,
    maxHeight:Int = 50,
    spacer:Int = 10,
    vendor: String,
    amount: String,
    quantity: String,
    subCategory: String,
    subCategoryTitle: String = "SubCategory:",
    dateTitle: String ="Date:",
    headingStyle: TextStyle = TypoH7,
    date: String,
    isExpanded :Boolean = false,
    onReceiptSelected: () -> Unit,
    onDeleteClicked:()->Unit,
    onButtonClick:()->Unit
) {
    RoundedBorderRectangle(
        modifier = modifier
            .fillMaxWidth().safeClickAble {
                onReceiptSelected()
            },
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f,
        borderThickness = 1f
    ) {
        Column( modifier = Modifier.padding(horizontal = 12.sdp, vertical = 8.sdp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier.size(iconSize.sdp),
                        painter = painterResource(iconId),
                        contentDescription = amount
                    )
                    if(quantity.isNotEmpty()){
                        VerticalSpacer(spacer)
                        Text(
                            text = quantity,
                            style = SubtitleLarge,
                            textAlign = TextAlign.Start,
                            color = secondaryColor
                        )
                    }
                }

                HorizontalSpacer(15)
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    // Amount Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(vendor.isNotEmpty()){
                            Text(
                                text = vendor,
                                style = headingStyle,
                                color = primaryColor
                            )
                            HorizontalSpacer(12)
                        }
                        Text(
                            text = amount,
                            style = headingStyle,
                            color = secondaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    VerticalSpacer(spacer)
                    // Subcategory Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = subCategoryTitle,
                            style = ButtonMedium,
                            color = textColor
                        )
                        HorizontalSpacer()
                        Text(
                            text = subCategory,
                            style = SubtitleMedium,
                            color = primaryColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    VerticalSpacer(spacer)
                    // Date Row
                    Row(
                        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dateTitle,
                            style = ButtonMedium,
                            color = textColor
                        )
                        HorizontalSpacer()
                        Text(
                            text = date,
                            style = SubtitleMedium,
                            color = primaryColor
                        )
                    }
                }
                Row(modifier = Modifier.height(maxHeight.sdp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    Image(modifier = Modifier
                        .size(12.sdp)
                        .padding(2.sdp),
                        painter = painterResource(if(isExpanded) R.drawable.icon_collapse else R.drawable.icon_expand),
                        contentDescription = if(isExpanded) "Collapse" else "Expand",
                        colorFilter = ColorFilter.tint(secondaryColor)
                    )
                }
                HorizontalSpacer(10)
            }
            if(isExpanded){
                VerticalSpacer(20)
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    SmallRoundedButton(false,
                        stringResource(R.string.discard), onButtonClick = {onDeleteClicked()})
                    HorizontalSpacer()
                    SmallRoundedButton(true, stringResource(R.string.add), onButtonClick = {onButtonClick()})
                }
            }
        }
    }
}