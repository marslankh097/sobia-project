package com.example.budgetly.ui.cash.home.screens.home.content

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.NativeAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.ui.cash.home.screens.pie_chart.content.DonutPieChart
import com.example.budgetly.ui.cash.home.state.PieSlice
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.TextWithUnderLine
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.bgShade1
import com.example.budgetly.utils.bgShade2
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.log
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.toSafeDouble
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CategoryContent(
    modifier: Modifier = Modifier, isExpense: Boolean = false,
    commonAdsUtil: CommonAdsUtil,
    categoryList: List<CategoryModel>,
    pieSlices: List<PieSlice>,
    balance:String,
    expense:String,
    income:String,
    currency:String,
    navigateToCategoryDisplay:()->Unit,
    onCategoryCardClick: (CategoryModel) -> Unit,
    onMainCardClick: () -> Unit,
    onChartClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.sdp), horizontalAlignment = Alignment.End
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(150.sdp).safeClickAble {
            onChartClick()
        },horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Column(modifier = Modifier.height(150.sdp).weight(1f) .verticalScroll(
                rememberScrollState(),true
            ), verticalArrangement = Arrangement.SpaceEvenly){
                BalanceRow(
                    modifier = Modifier.fillMaxWidth(),
                    balance = balance,
                    currency = currency
                )
                HorizontalDivider(color = greyShade1, thickness = 1.sdp, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.sdp))
                BalanceRow(
                    modifier = Modifier.fillMaxWidth(),
                    label = if(isExpense) "Total Expenses:" else "Total Income:",
                    balance = if(isExpense) expense else income,
                    currency = currency
                )
            }
            DonutPieChart(
                modifier = Modifier.size(150.sdp).padding(12.sdp),
                slices = pieSlices.filter { it.proportion > 0f },
                if(isExpense) "Expenses" else "Income"
            )
        }
        VerticalSpacer(8)
        MainCard(
            title = if (isExpense) stringResource(R.string.all_expenses) else stringResource(R.string.all_income),
            amount = categoryList.sumOf { it.transactionTotal.toSafeDouble() }.toString(),
            currency = categoryList.firstOrNull()?.currency?:"PKR",
            subTitle = if (isExpense) stringResource(R.string.view_all_expense_transactions) else stringResource(R.string.view_all_income_transactions),
            imageId = if (isExpense) R.drawable.img_expense else R.drawable.img_income,
            onCardClick = {
                // navigate To next transaction screen
                onMainCardClick()
            }
        )
        VerticalSpacer()
        NativeAd(
            context = context as Activity,
            adKey = if(isExpense) AdKeys.ExpenseNative.name else AdKeys.IncomeNative.name,
            placementKey = if(isExpense) RemoteConfigKeys.EXPENSE_NATIVE_ENABLED else RemoteConfigKeys.INCOME_NATIVE_ENABLED,
            layoutKey = if(isExpense) RemoteConfigKeys.EXPENSE_NATIVE_LAYOUT else RemoteConfigKeys.INCOME_NATIVE_LAYOUT,
            screenName = if(isExpense) "Expense_Main_Screen_Middle" else "Income_Main_Screen_Middle",
            commonAdsUtil = commonAdsUtil
        )
        VerticalSpacer()
        CategoryCards(
            isExpense = isExpense, displayCategories = categoryList.take(4),
            navigateToCategoryDisplay = navigateToCategoryDisplay,
            categoryCardClick = { category->
                log("CategoryCards:categoryCardClick:category :$category")
                onCategoryCardClick.invoke(category)
            }
        )
    }
}

@Composable
fun CategoryCards(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    displayCategories: List<CategoryModel>,
    navigateToCategoryDisplay:()->Unit,
    categoryCardClick: (CategoryModel) -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier,
        bgColor = itemBgColor,
        borderColor = Color.Transparent,
        borderRadius = 8f, borderThickness = 1f
    ) {
        Column(modifier = modifier
            .fillMaxWidth()
            .padding(12.sdp)
            .background(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to secondaryBgColor,
                        0.7f to bgShade1,
                        0.9f to bgShade2,
                        1.0f to itemBgColor
                    ),
                    radius = Float.POSITIVE_INFINITY, center = Offset.Unspecified
                ), shape = RoundedCornerShape(8.sdp)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().safeClickAble {
//                        navigate to all category display screen
                    navigateToCategoryDisplay()
                },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (isExpense) stringResource(R.string.expense_categories) else stringResource(
                        R.string.income_categories
                    ),
                    style = SubtitleLarge,
                    color = textColor
                )
                TextWithUnderLine(
                    modifier = Modifier.safeClickAble {
//                        navigate to all category display screen
                        navigateToCategoryDisplay()
                    },
                    text = "View All", style = SubtitleLarge, color = secondaryColor
                )
            }
            VerticalSpacer()
            // Create 2 rows of 2 cards each (total 4 cards)
            for (i in displayCategories.indices step 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.sdp)
                ) {
                    CategoryCard(
                        icon = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                        title = displayCategories[i].categoryName,
                        subTitle = displayCategories[i].transactionTotal,
                        currency = displayCategories[i].currency,
                        onCardClick = { categoryCardClick(displayCategories[i]) },
                        modifier = Modifier.weight(1f)
                    )
                    if (i + 1 < displayCategories.size) {
                        CategoryCard(
                            icon = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                            title = displayCategories[i + 1].categoryName,
                            subTitle = displayCategories[i + 1].transactionTotal,
                            currency = displayCategories[i + 1].currency,
                            onCardClick = { categoryCardClick(displayCategories[i + 1]) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f)) // Fill empty space if odd number
                    }
                }

                VerticalSpacer(10)
            }
//            LazyVerticalGrid(
//                modifier = Modifier.fillMaxWidth().wrapContentSize(),
//                columns = GridCells.Fixed(2),
//                verticalArrangement = Arrangement.spacedBy(10.sdp),
//                horizontalArrangement = Arrangement.spacedBy(10.sdp)
//            ) {
//                items(displayCategories) { category ->
//                    CategoryCard(
//                        icon = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
//                        title = category.categoryName,
//                        onCardClick = {
//                            // navigate To next transaction screen
//                            categoryCardClick()
//                        }
//                    )
//                }
//            }
        }
    }
}
