package com.example.budgetly.ui.cash.sort.state

import com.example.budgetly.domain.models.enums.sort.OrderBy
import com.example.budgetly.domain.models.enums.sort.SortBy

data class SortDisplayState(
    var sortBy: SortBy = SortBy.DateCreated,
    var orderBy: OrderBy = OrderBy.Descending,
    var tempSortBy: SortBy = SortBy.DateCreated,
    var tempOrderBy: OrderBy = OrderBy.Descending,
    val showSortingDialog:Boolean = false,
    )