package com.example.budgetly.domain.models.db.transactions

import com.example.budgetly.domain.models.enums.account.AccountCategory
import com.example.budgetly.domain.models.enums.account.AccountType

data class AccountModel(
    var accountId:Long = 0L,
    var accountIBAN:String,
    var accountType:String = AccountType.Cash.name,
    var accountCategory:String = AccountCategory.Personal.name,
    var balance:String,
    var initialBalance:String,
    var currency:String,
)
