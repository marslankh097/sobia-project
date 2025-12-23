package com.example.budgetly.domain.models.enums.transaction

import com.demo.budgetly.R

enum class  TransactionDuration(val days:Int, val  title:Int) {
   OneDay(1, R.string.one_day),
   SevenDays(7, R.string.one_week),
   OneMonth(30, R.string.one_month),
   ThreeMonths(90, R.string.three_months),
   SixMonths(180, R.string.six_months),
   OneYear(365, R.string.one_year),
   DateRange(0, R.string.date_range)
}