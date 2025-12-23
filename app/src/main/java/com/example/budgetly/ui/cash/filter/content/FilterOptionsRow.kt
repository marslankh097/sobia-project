package com.example.budgetly.ui.cash.filter.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.filter.FilterOptions
import com.example.budgetly.utils.greyShade1
import ir.kaaveh.sdpcompose.sdp

@Composable
fun FilterOptionsRow(modifier: Modifier = Modifier,
                     filterOptions: List<FilterOptions>,
                     isAllSelected:Boolean,
                     onAllClick:(Boolean)->Unit,
                     onOptionClick:(FilterOptions,Boolean)->Unit){
    Row(modifier = modifier.fillMaxWidth()){
        FilterOption(
            title = stringResource(R.string.all),
            isSelected = isAllSelected,
            onFilterOptionClick = {
                onAllClick.invoke(it)
            }
        )
        VerticalDivider(color = greyShade1, thickness = 1.sdp, modifier = Modifier.height(40.sdp).padding(vertical = 5.sdp))
        LazyRow(modifier = Modifier.fillMaxWidth().weight(1f)){
            items(filterOptions){ option->
                FilterOption(
                    title = stringResource(option.title),
                    isSelected = option.isSelected
                ){
                    onOptionClick.invoke(option,it)
                }
            }
        }
    }
}