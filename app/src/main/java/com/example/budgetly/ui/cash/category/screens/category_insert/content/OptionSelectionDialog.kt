package com.example.budgetly.ui.cash.category.screens.category_insert.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.dialog.BaseDialog
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import ir.kaaveh.sdpcompose.sdp

@Composable
fun OptionSelectionDialog(modifier: Modifier = Modifier,
                          title:String,
                          selectedOption:String,
                          options:List<String>,
                          onOptionSelected:(String)->Unit) {
    BaseDialog(onButtonClick = {onOptionSelected("")},dismissOnClickOutside = true, dismissOnBackPress = true) {
        RoundedBorderRectangle(
            modifier = modifier.padding(12.sdp),
            bgColor = itemBgColor,
            borderColor = Color.Transparent,
            borderRadius = 8f, borderThickness = 1f
        ){
            Column(modifier = modifier.padding(12.sdp), horizontalAlignment = Alignment.CenterHorizontally){
                VerticalSpacer()
                Text(
                    text = title,
                    color = secondaryColor,
                    style = SubtitleLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.sdp)
                )
                options.forEach { option->
                    VerticalSpacer()
                    CustomSelectionRow(
                        modifier = Modifier.fillMaxWidth(),
                        bgColor = secondaryBgColor,
                        title = option,
                        isExpandable = false,
                        style = SubtitleMedium,
                        isExpanded = option == selectedOption,
                        onCategorySelected = {onOptionSelected(option)}
                    )
                }
                VerticalSpacer()
            }
        }
    }
}