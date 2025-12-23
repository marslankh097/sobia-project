package com.example.budgetly.domain.models.enums.common

import com.demo.budgetly.R

enum class Options{
    Update,
    Delete
}
fun String.toOptions():Options{
    return enumValueOf<Options>(this)
}