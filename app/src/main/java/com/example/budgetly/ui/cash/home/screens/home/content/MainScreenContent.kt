package com.example.budgetly.ui.cash.home.screens.home.content
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.demo.budgetly.R
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.ui.cash.category.state.CategoryDisplayState
import com.example.budgetly.ui.cash.home.state.PieChartState
import com.example.budgetly.ui.cash.transaction.state.TransactionDisplayState
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.toSafeDouble

@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    commonAdsUtil: CommonAdsUtil,
    isExpense: Boolean,
    categoryState: CategoryDisplayState,
    transactionDisplayState: TransactionDisplayState,
    pieChartState: PieChartState,
    navigateToCategoryDisplay:()->Unit,
    onCategoryCardClick: (CategoryModel?, SubCategoryModel?) -> Unit,
    onMainCardClick:()->Unit,
    onChartClick:()->Unit,
    tryAgainClick:(Boolean)->Unit
) {
    if(categoryState.isLoading || pieChartState.isLoading ){
        LoadingComponent(textId = if (isExpense) R.string.loading_expense_categories else R.string.loading_income_categories)
    }else{
        if(categoryState.error != null){
            FailureComponent(
                msg = categoryState.error,
                onTryAgainClick = {
                    tryAgainClick.invoke(true)
                }
            )
        }else if(pieChartState.error != null){
            FailureComponent(
                msg = pieChartState.error,
                onTryAgainClick = {
                    tryAgainClick.invoke(false)
                }
            )
        }else{
            val categoryList =
                if (isExpense) categoryState.expenseCategories else categoryState.incomeCategories
            Column(modifier = modifier.fillMaxWidth()) {
                CategoryContent(
                    modifier = Modifier
                        .fillMaxWidth(), isExpense = isExpense,
                    categoryList = categoryList,
                    commonAdsUtil = commonAdsUtil,
                    balance = transactionDisplayState.accountModel?.balance ?: "0.0",
                    currency = transactionDisplayState.accountModel?.currency ?: "PKR",
                    expense = categoryState.expenseCategories.sumOf { it.transactionTotal.toSafeDouble() }.toString(),
                    income = categoryState.incomeCategories.sumOf { it.transactionTotal.toSafeDouble() }.toString(),
                    pieSlices = if (isExpense) pieChartState.expenseSlices else pieChartState.incomeSlices,
                    navigateToCategoryDisplay = navigateToCategoryDisplay,
                    onCategoryCardClick = { category->
                        val firstSubCategory = categoryState.subCategoriesMap[category.categoryId]?.firstOrNull()
                        onCategoryCardClick(category, firstSubCategory)
                                          },
                    onMainCardClick = { onMainCardClick() },
                    onChartClick = onChartClick
                )
            }
        }
    }
}