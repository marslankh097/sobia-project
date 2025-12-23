package com.example.budgetly.ui.cash.sort.content

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.sort.OrderBy
import com.example.budgetly.domain.models.enums.sort.SortBy
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.LabelLarge
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.dialog.BaseBottomSheetDialog
import com.example.budgetly.utils.grey
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.inactiveButtonColor
import com.example.budgetly.utils.inactiveTextColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.SecondaryButtonRounded
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun SortBottomSheetDialog(
    modifier: Modifier = Modifier,
    currentSortBy: SortBy,
    currentOrderBy: OrderBy,
    onSortBySelected: (SortBy) -> Unit,
    onOrderBySelected: (OrderBy) -> Unit,
    onButtonClick: (Boolean) -> Unit
) {
    BaseBottomSheetDialog(
        onDismissRequest = {  onButtonClick.invoke(false)  }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(itemBgColor)
                .padding(12.sdp)
        ) {
            VerticalSpacer()
            Text(
                text = stringResource(R.string.sort_by),
                style = SubtitleLarge,
                color = secondaryColor
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SortBy.entries.forEach {
                    SortOptionItem(
                        icon = it.iconId,
                        text = stringResource(it.titleId),
                        isSelected = currentSortBy == it,
                        onClick = { onSortBySelected(it) }
                    )
                }
            }

            VerticalSpacer()
            HorizontalDivider(color = greyShade1, thickness = 1.sdp, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.sdp))
            VerticalSpacer()
            Text(
                text = stringResource(R.string.order_by),
                style = SubtitleLarge,
                color = secondaryColor
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OrderBy.entries.forEach {
                    SortOptionItem(
                        icon = it.iconId,
                        text = stringResource(it.titleId),
                        isSelected = currentOrderBy == it,
                        onClick = { onOrderBySelected(it) }
                    )
                }
            }
            VerticalSpacer(20)
            Row(modifier = Modifier.fillMaxWidth()) {
                SecondaryButtonRounded(modifier = Modifier
                    .weight(1f),
                    horizontalPadding = 10,
                    transparentColors = listOf(inactiveButtonColor, inactiveButtonColor),
                    gradientColors = listOf(inactiveTextColor, inactiveTextColor),
                    onButtonClick = { onButtonClick.invoke(false) }) {
                    Text(
                        modifier = Modifier.scrollable(
                            state = rememberScrollState(),
                            orientation = Orientation.Horizontal,
                        ), text = stringResource(R.string.cancel), color = grey, style = ButtonMedium,
                    )
                }
                PrimaryButtonRounded(modifier = Modifier
                    .weight(1f),
                    horizontalPadding = 10,
                    onButtonClick = { onButtonClick.invoke(true) }) {
                    Text(text = stringResource(R.string.apply), color = white, style = LabelLarge)
                }
            }
        }
    }
}
