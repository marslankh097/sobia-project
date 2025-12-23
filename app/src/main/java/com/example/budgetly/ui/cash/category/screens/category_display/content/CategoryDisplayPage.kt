package com.example.budgetly.ui.cash.category.screens.category_display.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.demo.budgetly.R
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.ui.cash.category.state.CategoryDisplayState
import com.example.budgetly.utils.ThreeDots
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.shared_components.EmptyComponent
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CategoryDisplayPage(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    categoryState: CategoryDisplayState,
    searchText: String,
    expenseExpandedCategoryIds: List<Long>,
    incomeExpandedCategoryIds: List<Long>,
    onCategoryExpanded: (Boolean,Long) -> Unit,
    expandCategories: (Boolean, List<Long>) -> Unit,
    onLongPressCategory: (CategoryModel) -> Unit,
    onLongPressSubCategory: (SubCategoryModel, CategoryModel) -> Unit,
    onCategorySelected: (CategoryModel, SubCategoryModel?) -> Unit,
    onSubCategorySelected: (CategoryModel, SubCategoryModel) -> Unit
) {
   val expandedCategoryIds =  if(isExpense) expenseExpandedCategoryIds else incomeExpandedCategoryIds
    val categories = if (isExpense) categoryState.expenseCategories else categoryState.incomeCategories

    // Filtered categories and expanded state
    val filteredCategories = remember(searchText, categoryState.isLoading, categoryState.error,categories
//        categoryState.expenseCategories, categoryState.incomeCategories
        , categoryState.subCategoriesMap) {
        val trimmedSearch = searchText.trim()
        val matchingIds = expandedCategoryIds.toMutableList()
        val result = if (!categoryState.isLoading && categoryState.error == null) {
            if (trimmedSearch.isNotEmpty()) {
                matchingIds.clear()
                val mappedCategories = categories.mapNotNull { category ->
                    val subCategories = categoryState.subCategoriesMap[category.categoryId].orEmpty()
                    val categoryMatches = category.categoryName.contains(trimmedSearch, ignoreCase = true)
                    val matchingSubCategories = subCategories.filter {
                        it.subCategoryName.contains(trimmedSearch, ignoreCase = true)
                    }
                    when {
                        categoryMatches -> {
                            matchingIds.add(category.categoryId)
                            category
                        }

                        matchingSubCategories.isNotEmpty() -> {
                            matchingIds.add(category.categoryId)
                            category
                        }

                        else -> null
                    }
                }
                mappedCategories
            } else{
                categories
            }
        } else emptyList()
        expandCategories(isExpense, matchingIds)
        result
    }
    Column(modifier = modifier.fillMaxWidth()){
        if (filteredCategories.isEmpty()) {
            EmptyComponent(
                modifier = Modifier.fillMaxSize(),
                image = R.drawable.img_no_category_available, text = if(isExpense) R.string.no_expense_categories_available
                else R.string.no_income_categories_available
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.sdp, vertical = 12.sdp).weight(1f),
                contentPadding = PaddingValues(bottom = 60.sdp),
                verticalArrangement = Arrangement.spacedBy(10.sdp)
            ) { items(filteredCategories) { category ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    CategoryRow(
                        modifier = Modifier.fillMaxWidth(),
                        title = category.categoryName,
                        subTitle = category.transactionTotal,
                        currency = category.currency,
                        iconId = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                        isExpanded = category.categoryId in expandedCategoryIds,
                        searchText = searchText,
                        onCategorySelected = {
                            val firstSub = categoryState.subCategoriesMap[category.categoryId]?.firstOrNull()
                            onCategorySelected(category, firstSub)
                        },
                        onCategoryExpanded = {
                            onCategoryExpanded(isExpense, category.categoryId)
                        },
                        onLongPressed = {
                            onLongPressCategory(category)
                        }
                    )

                    if (category.categoryId in expandedCategoryIds) {
                        VerticalSpacer()
                        val subCategories = categoryState.subCategoriesMap[category.categoryId].orEmpty()
                        val visibleSubCategories = if (searchText.isNotBlank()) {
                            subCategories.filter {
                                it.subCategoryName.contains(searchText.trim(), ignoreCase = true)
                            }
                        } else subCategories

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.sdp),
                            verticalArrangement = Arrangement.spacedBy(10.sdp)
                        ) {
                            visibleSubCategories.forEach { subCategory ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ThreeDots()
                                    SubCategoryRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        iconId = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                                        title = subCategory.subCategoryName,
                                        subTitle = subCategory.transactionTotal,
                                        currency = subCategory.currency,
                                        searchText = searchText,
                                        onSubCategorySelected = {
                                            onSubCategorySelected(category, subCategory)
                                        },
                                        onLongPressed = {
                                            onLongPressSubCategory(subCategory, category)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            }
        }
    }
}
