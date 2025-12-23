package com.example.budgetly.ui.cash.filter.events

import com.example.budgetly.domain.models.enums.transaction.TransactionDuration

sealed class FilterEvent {
    data class SetTempFromTimeStamp(val timeStamp: Long) : FilterEvent()
    data class SetTempToTimeStamp(val timeStamp: Long) : FilterEvent()
    data class UseCustomDateRange(val use: Boolean) : FilterEvent()
    data class UseAmountRange(val use: Boolean) : FilterEvent()
    data class SetTimeStamp(val fromTimeStamp:Long, val toTimeStamp:Long) : FilterEvent()
    data class ShowDurationSelectionDialog(val show: Boolean) : FilterEvent()
    data class SetScreenSource(val source:String) : FilterEvent()
    data class SetTransactionDuration(val transactionDuration: TransactionDuration) : FilterEvent()
    data class ShowFromCalendarDialog(val show: Boolean) : FilterEvent()
    data class ShowToCalendarDialog(val show: Boolean) : FilterEvent()
}
