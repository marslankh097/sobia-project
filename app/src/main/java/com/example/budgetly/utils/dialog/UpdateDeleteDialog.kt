package com.example.budgetly.utils.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.common.Options
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.TypoH7
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.shared_components.SimpleRow
import ir.kaaveh.sdpcompose.sdp

@Composable
fun UpdateDeleteDialog(modifier: Modifier = Modifier, title:String, onOptionSelected:(String)->Unit) {
    BaseDialog(onButtonClick = {onOptionSelected("")}, dismissOnClickOutside = true, dismissOnBackPress = true) {
        RoundedBorderRectangle(
            modifier = modifier.padding(12.sdp),
            bgColor = itemBgColor,
            borderColor = Color.Transparent,
            borderRadius = 8f, borderThickness = 1f
        ){
            Column(modifier = modifier.padding(15.sdp), horizontalAlignment = Alignment.CenterHorizontally){
                VerticalSpacer(12)
                Text(
                    text = title,
                    color = secondaryColor,
                    style = TypoH7,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 10.sdp)
                )
                val options = Options.entries.map { it.name }
                options.forEach { option->
                    VerticalSpacer(12)
                    SimpleRow(
                        modifier = Modifier.fillMaxWidth(),
                        title = option,
                        imageId = if(option == Options.Delete.name)
                            R.drawable.img_delete else R.drawable.img_update,
                        bgColor = secondaryBgColor,
                        horizontalSpacer = 20,
                        imageSize = 35,
                        style = SubtitleLarge,
                         onClick = {onOptionSelected(option)}
                    )
                }
                VerticalSpacer(12)
            }
        }
    }
}