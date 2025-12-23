package com.example.budgetly.ui.cash.filter.state

import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.domain.models.enums.transaction.TransactionScreenSource

data class FilterDisplayState(

    var screenSource:String = TransactionScreenSource.All.name,

    var useCustomDateRange:Boolean = false,

    var transactionDuration: TransactionDuration = TransactionDuration.OneMonth,
    val showDurationSelectionDialog:Boolean = false,


    var tempFromTimeStamp:Long = 0,
    var tempToTimeStamp:Long = 0,

    val showFromCalenderDialog:Boolean = false,
    val showToCalenderDialog:Boolean = false,

    var fromTimeStamp:Long = 0,
    var toTimeStamp:Long = 0,

    var useAmountRange:Boolean = false,

    var tempFromAmount:String = "",
    var tempToAmount:String = "",

    var fromAmount:String = "",
    var toAmount:String = "",

    val accountId: Long?= null,
    val categoryId: Long?= null,
    val subcategoryId: Long?= null,
    )
