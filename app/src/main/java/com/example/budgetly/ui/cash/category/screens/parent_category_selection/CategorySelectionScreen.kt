package com.example.budgetly.ui.cash.category.screens.parent_category_selection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.screens.category_display.content.CategoryRow
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.events.TransactionInsertEvent
import com.example.budgetly.utils.log
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CategorySelectionScreen(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    navigateBack: () -> Unit,
    fromTransaction: Boolean = false,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionsViewModel: TransactionsViewModel = hiltViewModel()
) {
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }
    val categoryState by categoryViewModel.state.collectAsState()
    val insertState by categoryViewModel.insertState.collectAsState()
    val transactionInsertState by transactionsViewModel.insertState.collectAsState()
    val tempParentCategory by categoryViewModel.tempParentCategory.collectAsState()
//    val tempCategory by transactionsViewModel.tempCategory.collectAsState()
//    val tempSubCategory by transactionsViewModel.tempSubCategory.collectAsState()
    val categories =
        if (isExpense) categoryState.expenseCategories else categoryState.incomeCategories
    val subCategories = categoryState.subCategoriesMap[transactionInsertState.categoryId] ?: emptyList()
    val categoryList = if (fromTransaction) {
        if (transactionInsertState.isUpdateMode) {
            categories
        } else {
            listOf(null) + categories
        }
    } else {
        if (insertState.isUpdateMode) {
            categories
        } else {
            listOf(null) + categories
        }
    }
    val subCategoryList = if (transactionInsertState.isUpdateMode) {
        subCategories
    } else {
        listOf(null) + subCategories
    }
    Column(modifier.fillMaxSize()) {
        TopBar(
            title = if (fromTransaction) {
                if (transactionInsertState.selectCategory) {
                    "Select Transaction Category"
                } else {
//                    "Select ${transactionInsertState.categoryModel?.categoryName?:""} SubCategory"
                    "Select SubCategory"
                }
            } else {
                "Select Parent Category"
            },
            showApply = true,
            onButtonClick = {
                // Send selected ID back only on Apply
                if (fromTransaction) {
                    if (transactionInsertState.selectCategory) {
                        log("tempCategory:${transactionInsertState.tempCategory}")
                        transactionsViewModel.onEvent(
                            TransactionInsertEvent.SetCategory(
                                category = transactionInsertState.tempCategory
                            )
                        )
                        val firstSubCategory = categoryState.subCategoriesMap[transactionInsertState.tempCategory?.categoryId]?.firstOrNull()
                        transactionsViewModel.onEvent(
                            TransactionInsertEvent.SetSubCategory(
                                subCategory = firstSubCategory
                            )
                        )
                    } else {
                        log("tempSubCategory:${transactionInsertState.tempSubCategory}")
                        transactionsViewModel.onEvent(
                            TransactionInsertEvent.SetSubCategory(
                                subCategory = transactionInsertState.tempSubCategory
                            )
                        )
                    }
                } else {
                    log("tempParentCategory:$tempParentCategory")
                    categoryViewModel.onEvent(
                        CategoryEvent.SetParentCategory(
                            id = tempParentCategory.first,
                            name = tempParentCategory.second,
                        )
                    )
                }
                handleBack.invoke()
            }
        ) {
            handleBack()
        }

        when {
            categoryState.isLoading -> {
                LoadingComponent(
                    textId =
                        if (fromTransaction) {
                            if (transactionInsertState.selectCategory) R.string.loading_categories else R.string.loading_subcategories
                        } else {
                            if (isExpense) R.string.loading_expense_categories else R.string.loading_income_categories
                        }

                )
            }

            categoryState.error != null -> {
                FailureComponent(
                    msg = categoryState.error ?: "",
                    onTryAgainClick = {
                        categoryViewModel.onEvent(CategoryEvent.LoadCategories)
                    }
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(secondaryBgColor)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (categoryList.isEmpty()) {
                            EmptyComponent(
                                modifier = Modifier.fillMaxSize(),
                                image = R.drawable.img_no_category_available,
                                text = if (fromTransaction) {
                                    if (transactionInsertState.selectCategory) {
                                        R.string.no_categories_created_yet
                                    } else {
                                        R.string.no_subcategories_created_yet
                                    }
                                } else {
                                    R.string.no_transactions_available
                                }
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 12.sdp, vertical = 12.sdp),
                                contentPadding = PaddingValues(bottom = 60.sdp),
                                verticalArrangement = Arrangement.spacedBy(10.sdp)
                            ) {
                                if (fromTransaction && transactionInsertState.selectCategory.not()) {
                                    items(subCategoryList) { subCategory ->
                                        val isSelected =
                                            subCategory?.subCategoryId == transactionInsertState.tempSubCategory?.subCategoryId
                                        CategoryRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            title = subCategory?.subCategoryName ?: "None",
                                            isExpandable = false,
                                            iconId = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                                            isExpanded = isSelected,
                                            searchText = "",
                                            onCategorySelected = {
                                                transactionsViewModel.onEvent(
                                                    TransactionInsertEvent.SetTempSubCategory(
                                                        subCategory
                                                    )
                                                )
                                            }
                                        )
                                    }
                                } else {
                                    items(categoryList) { category ->
                                        val isSelected =
                                            category?.categoryId ==
                                                    if (fromTransaction) {
                                                        transactionInsertState.tempCategory?.categoryId
                                                    }else{
                                                        tempParentCategory.first
                                                    }
                                        CategoryRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            title = category?.categoryName ?: "None",
                                            isExpandable = false,
                                            iconId = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                                            isExpanded = isSelected,
                                            searchText = "",
                                            onCategorySelected = {
                                                if (fromTransaction) {
                                                    transactionsViewModel.onEvent(
                                                        TransactionInsertEvent.SetTempCategory(
                                                            category
                                                        )
                                                    )
                                                } else {
                                                    categoryViewModel.onEvent(
                                                        CategoryEvent.UpdateTempParentCategory(
                                                            name = category?.categoryName?:"None",
                                                            id = category?.categoryId
                                                        )
                                                    )
                                                }
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

