package com.example.budgetly.domain.models.enums.sort

import com.demo.budgetly.R

enum class OrderBy(val titleId:Int, val iconId:Int) {
    Ascending(R.string.ascending, R.drawable.icon_ascending),
    Descending(R.string.descending, R.drawable.icon_descending)
}