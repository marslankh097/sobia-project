package com.example.budgetly.ui.banking.transactions.content
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.demo.budgetly.R
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleSmall
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun TransactionRow(
    modifier: Modifier = Modifier,
    iconId:Int,
    title: String,
    subTitle: String,
    style: TextStyle = SubtitleSmall,
    onTransactionSelected: () -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier.safeClickAble {
            onTransactionSelected.invoke()
        },
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Row(modifier = modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier.size(40.sdp), painter = painterResource(iconId), contentDescription = title)
            HorizontalSpacer(12)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), verticalArrangement = Arrangement.SpaceBetween
            ){
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = stringResource(R.string.amount_),
                        style = ButtonMedium,
                        textAlign = TextAlign.Start,
                        color = textColor
                    )
                    HorizontalSpacer(8)
                    Text(
                        text = title,
                        style = style,
                        textAlign = TextAlign.Start,
                        color = primaryColor
                    )
                }
                Row ( verticalAlignment = Alignment.Bottom){
                    Text(
                        text = stringResource(R.string.date),
                        style = ButtonMedium,
                        textAlign = TextAlign.Start,
                        color = textColor
                    )
                    HorizontalSpacer(8)
                    Text(
                        text = subTitle,
                        style = style,
                        textAlign = TextAlign.Start,
                        color = secondaryColor
                    )
                }
            }
            HorizontalSpacer(10)
        }
    }
}