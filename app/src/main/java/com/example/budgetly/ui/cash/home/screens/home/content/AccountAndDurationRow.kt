package com.example.budgetly.ui.cash.home.screens.home.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.budgetly.ui.cash.category.screens.category_insert.content.CustomSelectionRow
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun AccountAndDurationRow(
    modifier: Modifier = Modifier,
    accountTitle: String,
    accountCurrency: String,
    durationTitle: String,
    isEnabled :Boolean = true,
    onAccountClick: (Boolean) -> Unit,
    onDurationClick: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.sdp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomSelectionRow(
            modifier = Modifier.width(120.sdp),
            title = accountTitle,
            currency = accountCurrency,
            isExpanded = false,
            bgColor = if(isEnabled) itemBgColor else greyShade1,
            txtColor = if(isEnabled) textColor else hintColor,
            tintColor = if(isEnabled) secondaryColor else hintColor,
            onCategorySelected = onAccountClick
        )
        CustomSelectionRow(
            modifier = Modifier.width(120.sdp),
            title = durationTitle,
            currency = "",
            isExpanded = false,
            bgColor = if(isEnabled) itemBgColor else greyShade1,
            txtColor = if(isEnabled) textColor else hintColor,
            tintColor = if(isEnabled) secondaryColor else hintColor,
            onCategorySelected = onDurationClick
        )
    }
}
