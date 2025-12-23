package com.example.budgetly.ui.banking.institution.content
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.demo.budgetly.R
import com.example.budgetly.utils.Caption1
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.shared_components.CoilImage
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun InstitutionRow(
    modifier: Modifier = Modifier,
    icon: String?,
    title: String,
    subTitle: String,
    style: TextStyle = SubtitleSmall,
    onInstitutionSelected: () -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble {
            onInstitutionSelected.invoke()
        },
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Row(modifier = modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
            CoilImage(modifier = Modifier.size(30.sdp), model = icon, placeholder = R.drawable.img_bank)
            HorizontalSpacer(12)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = title.toSafeEmptyString(),
                    style = style,
                    textAlign = TextAlign.Start,
                    color = textColor
                )
                VerticalSpacer(5)
                Text(
                    text = subTitle.toSafeEmptyString(),
                    style = Caption1,
                    textAlign = TextAlign.Start,
                    color = hintColor
                )
            }
            HorizontalSpacer(10)
            Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
//                Image(painter = R.drawable.icon_tick, contentDescription = "")
            }
        }
    }
}