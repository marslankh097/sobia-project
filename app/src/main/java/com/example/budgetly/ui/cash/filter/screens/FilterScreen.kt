package com.example.budgetly.ui.cash.filter.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.filter.events.FilterEvent
import com.example.budgetly.ui.cash.home.screens.home.content.AccountAndDurationRow
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.utils.Utils
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.dialog.CalendarDialog
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.log
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.SimpleRow
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toDateString
import com.example.budgetly.utils.toast
import ir.kaaveh.sdpcompose.sdp

@Composable
fun FilterScreen(modifier: Modifier = Modifier, navigateBack:()->Unit,
                 transactionsViewModel: TransactionsViewModel = hiltViewModel(),
                 categoryViewModel: CategoryViewModel = hiltViewModel()
){
    val insertState by transactionsViewModel.insertState.collectAsState()
    val displayState by transactionsViewModel.state.collectAsState()
    val filterState by transactionsViewModel.filterState.collectAsState()
//    val tempFrom by transactionsViewModel.tempFromTimeStamp.collectAsState()
//    val tempTo by transactionsViewModel.tempToTimeStamp.collectAsState()
    val handleBack = {
        if(filterState.transactionDuration != TransactionDuration.DateRange && filterState.useCustomDateRange){
            transactionsViewModel.onEvent(FilterEvent.UseCustomDateRange(false))
        }
        navigateBack()
    }
    BackHandler { handleBack() }
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(title = "Filter By", showApply = true,
            onButtonClick = {
                // Send selected ID back only on Apply
                log("tempFrom:${filterState.tempFromTimeStamp.toDateString()} : tempTo:${filterState.tempToTimeStamp.toDateString()}")
                if(filterState.useCustomDateRange){
                    transactionsViewModel.onEvent(FilterEvent.SetTransactionDuration(TransactionDuration.DateRange))
                    transactionsViewModel.onEvent(FilterEvent.SetTimeStamp(filterState.tempFromTimeStamp, filterState.tempToTimeStamp))
                    categoryViewModel.onEvent(CategoryEvent.SetTimeStamp(filterState.tempFromTimeStamp, filterState.tempToTimeStamp))
                }else{
                    if(filterState.transactionDuration == TransactionDuration.DateRange){
                        val duration = TransactionDuration.OneMonth
                        val (from, to) = Utils.getTimeStampsFromDays(duration.days)
                        transactionsViewModel.onEvent(FilterEvent.SetTransactionDuration(duration))
                        transactionsViewModel.onEvent(FilterEvent.SetTimeStamp(from, to))
                        categoryViewModel.onEvent(CategoryEvent.SetTimeStamp(from, to))
                    }
                }
                navigateBack()
            }
        ) {
            handleBack()
        }
        if(filterState.showFromCalenderDialog || filterState.showToCalenderDialog ){
            CalendarDialog(
                modifier = Modifier.fillMaxWidth(),
                initialDateMillis =  if(filterState.showFromCalenderDialog) filterState.tempFromTimeStamp else filterState.tempToTimeStamp,
                 onDateSelected = {
                     if(filterState.showFromCalenderDialog){
                         transactionsViewModel.onEvent(FilterEvent.SetTempFromTimeStamp(it))
                         transactionsViewModel.onEvent(FilterEvent.ShowFromCalendarDialog(false))
                     }else{
                         transactionsViewModel.onEvent(FilterEvent.SetTempToTimeStamp(it))
                         transactionsViewModel.onEvent(FilterEvent.ShowToCalendarDialog(false))
                     }

                 },
                onDismiss = {
                    if(filterState.showFromCalenderDialog){
                        transactionsViewModel.onEvent(FilterEvent.ShowFromCalendarDialog(false))
                    }else{
                        transactionsViewModel.onEvent(FilterEvent.ShowToCalendarDialog(false))
                    }
                }
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .background(secondaryBgColor).weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ){
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.sdp), verticalAlignment = Alignment.CenterVertically){
                Image(
                    painter = painterResource(id = if(filterState.useCustomDateRange){
                        R.drawable.icon_checkboc_selected
                    }else{
                        R.drawable.icon_checkboc_unselected
                    }),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.sdp).safeClickAble {
                            val isSelected = filterState.useCustomDateRange.not()
                           transactionsViewModel.onEvent(FilterEvent.UseCustomDateRange(isSelected))
                        },
                    colorFilter = if(filterState.useCustomDateRange) ColorFilter.tint(secondaryColor) else ColorFilter.tint(
                        hintColor)
                )
                SimpleRow(
                    bgColor = Color.Transparent,
                    imageSize = 20 ,
                    modifier = Modifier.fillMaxWidth(), title = stringResource(R.string.filter_by_date_range),
                    imageId = R.drawable.icon_sort_by_date, applyTint = true,
                    tintColor = if(filterState.useCustomDateRange) primaryColor else hintColor,
                    txtColor = if(filterState.useCustomDateRange) textColor else hintColor,
                )
            }
            VerticalSpacer(8)
            AccountAndDurationRow(
                accountTitle = filterState.tempFromTimeStamp.toDateString("dd-MM-yyyy") ,
                accountCurrency = "",
                durationTitle =  filterState.tempToTimeStamp.toDateString("dd-MM-yyyy"),
                onAccountClick = {
                    //open calender
                    if(filterState.useCustomDateRange){
                        transactionsViewModel.onEvent(FilterEvent.ShowFromCalendarDialog(true))
                    }else{
                        context.toast("Kindly tick the checkbox to enable filter by date range.")
                    }

                },
                onDurationClick = {
                    //open calender
                    if(filterState.useCustomDateRange){
                        transactionsViewModel.onEvent(FilterEvent.ShowToCalendarDialog(true))
                    }else{
                        context.toast("Kindly tick the checkbox to enable filter by date range.")
                    }
                },
                isEnabled = filterState.useCustomDateRange
            )
            HorizontalDivider(color = greyShade1, thickness = 1.sdp, modifier = Modifier.width(60.sdp).padding(horizontal = 12.sdp))
            VerticalSpacer(8)
//            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.sdp), verticalAlignment = Alignment.CenterVertically){
//                Image(
//                    painter = painterResource(id = if(filterState.useAmountRange){
//                        R.drawable.icon_checkboc_selected
//                    }else{
//                        R.drawable.icon_checkboc_unselected
//                    }),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(24.sdp).safeClickAble {
//                            val isSelected = filterState.useAmountRange.not()
//                            transactionsViewModel.onEvent(FilterEvent.UseAmountRange(isSelected))
//                        },
//                    colorFilter = if(filterState.useAmountRange) ColorFilter.tint(secondaryColor) else ColorFilter.tint(
//                        hintColor)
//                )
//                SimpleRow(
//                    bgColor = Color.Transparent,
//                    imageSize = 20 ,
//                    modifier = Modifier.fillMaxWidth(), title = stringResource(R.string.filter_by_date_range),
//                    imageId = R.drawable.icon_sort_by_amount, applyTint = true,
//                    tintColor = if(filterState.useAmountRange) primaryColor else hintColor,
//                    txtColor = if(filterState.useAmountRange) textColor else hintColor,
//                )
//            }
//            VerticalSpacer(8)

        }
    }
}