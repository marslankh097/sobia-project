package com.example.budgetly.ui.banking.transactions.events

sealed class TransactionEvent {
    data class LoadAllTransactions(val accountId: String, val fromDate: String?= null, val toDate: String?=null) : TransactionEvent()
//    data class LoadIncomeTransactions(val accountId: String, val fromDate: String?= null, val toDate: String?=null) : TransactionEvent()
//    data class LoadExpenseTransactions(val accountId: String, val fromDate: String?= null, val toDate: String?=null) : TransactionEvent()
    data class ChangeTab(val index: Int) : TransactionEvent()
}