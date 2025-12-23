package com.example.budgetly.ui.cash.sort.events

import com.example.budgetly.domain.models.enums.sort.OrderBy
import com.example.budgetly.domain.models.enums.sort.SortBy

sealed class SortEvent {
    data class ShowSortingDialog(val show: Boolean) : SortEvent()
    data class SetSortBy(val sortBy: SortBy) : SortEvent()
    data class SetOrderBy(val orderBy: OrderBy) : SortEvent()
    data class SetTempSortBy(val sortBy: SortBy) : SortEvent()
    data class SetTempOrderBy(val orderBy: OrderBy) : SortEvent()
}