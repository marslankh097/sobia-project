package com.example.budgetly.domain.models.enums.sort

import com.demo.budgetly.R

enum class SortBy(val titleId:Int, val iconId:Int){
    Amount(R.string.amount, R.drawable.icon_sort_by_amount),
    DateCreated (R.string.date_created, R.drawable.icon_sort_by_date)
}