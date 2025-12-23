package com.example.budgetly.ui.banking.transactions.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.bgShade2
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SwitchTabsRow(modifier: Modifier =Modifier, selectedIndex:Int, onClick:(Int)->Unit) {
    Row(modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.sdp)
                .background(bgShade2, shape = RoundedCornerShape(8.sdp))
                .padding(horizontal = 6.sdp, vertical = 6.sdp),
            horizontalArrangement = Arrangement.SpaceAround
    ) {
            TransactionTabs.entries.forEachIndexed { index, entry ->
                TabBox(Modifier
                    .weight(1f), tabText = stringResource(id = entry.titleId), selectedTab =  index == selectedIndex,
                    onClick = {onClick.invoke(index)})
            }
        }
}
@Composable
fun TabBox(modifier: Modifier = Modifier, tabText:String, selectedTab:Boolean, onClick: () -> Unit){
    Box(
        modifier = modifier
            .safeClickAble { onClick.invoke() }.clip(RoundedCornerShape(6.sdp)).background(color = if (selectedTab) secondaryColor else bgShade2)
            .padding(horizontal = 8.sdp, vertical = 8.sdp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(1f),
            text = tabText,
            style = SubtitleMedium,
            textAlign = TextAlign.Center,
            color =  if (selectedTab) white else hintColor
        )
    }
}