package com.example.budgetly.ui.cash.home.screens.pie_chart
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.NativeAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.home.screens.pie_chart.content.DonutPieChart
import com.example.budgetly.ui.cash.home.screens.pie_chart.content.PieChartRow
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import ir.kaaveh.sdpcompose.sdp

@Composable
fun PieChartDetailScreen(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    isSubcategoryView:Boolean = false,
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
    navigateToSubCategoryPieChart:(Boolean)->Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    commonAdsUtil: CommonAdsUtil,
    navigateBack: () -> Unit
) {
    val transactionState by transactionsViewModel.state.collectAsState()
    val categoryPieChartState by categoryViewModel.categoryPieChartState.collectAsState()
    val subcategoryPieChartState by categoryViewModel.subcategoryPieChartState.collectAsState()
    val pieChartDetailState =  if(isSubcategoryView) subcategoryPieChartState else categoryPieChartState
    val context = LocalContext.current
    val handleBack = {
        navigateBack()
    }
    BackHandler {
        handleBack()
    }
    Column(modifier = modifier.fillMaxSize()) {
        val title = stringResource(
            if (isExpense) R.string.expense_categories else R.string.income_categories
        )
        val screenTitle = if (isSubcategoryView) {
            subcategoryPieChartState.screenTitle
        } else title

        TopBar(screenTitle) { handleBack() }
        Column(modifier = Modifier.fillMaxWidth().weight(1f)){
            when {
                // 🔄 Show Loading
                pieChartDetailState.isLoading -> {
                    LoadingComponent(
                        textId =
                            if (isExpense) R.string.loading_expense_categories else R.string.loading_income_categories
                    )
                }

                // ⚠️ Show Error
                pieChartDetailState.error != null -> {
                    FailureComponent(
                        msg = pieChartDetailState.error,
                        onTryAgainClick = {
                            categoryViewModel.onEvent(CategoryEvent.LoadPieChart)
                        }
                    )
                }

                // ✅ Show Pie Chart content
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = secondaryBgColor),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VerticalSpacer(8)
                        DonutPieChart(
                            modifier = Modifier
                                .size(180.sdp)
                                .padding(12.sdp),
                            slices =  pieChartDetailState.slices.filter { it.proportion > 0f },
                            emptyLabel = stringResource(
                                if (isExpense) R.string.expense else R.string.income
                            )
                        )
                        VerticalSpacer()
                        val slices = pieChartDetailState.slices
                        val totalAmount = slices.sumOf { it.amount }
                        if(slices.isEmpty()){
                            EmptyComponent(
                                modifier = Modifier.fillMaxSize(),
                                image = R.drawable.img_no_category_available,
                                text = if(isExpense)
                                    R.string.no_expense_categories_available
                                else R.string.no_income_categories_available
                            )
                        }else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 12.sdp, vertical = 12.sdp),
                                contentPadding = PaddingValues(bottom = 60.sdp),
                                verticalArrangement = Arrangement.spacedBy(10.sdp)
                            ) {
                                items(slices) { slice ->
                                    val percentage =
                                        if (totalAmount != 0.0) (slice.amount / totalAmount) * 100 else 0.0
                                    PieChartRow(
                                        categoryName = slice.label,
                                        amount = slice.amount,
                                        percentage = percentage,
                                        currency = transactionState.accountModel?.currency
                                            ?: "PKR",
                                        onClick = {
                                            if (!isSubcategoryView) {
                                                categoryViewModel.onEvent(
                                                    CategoryEvent.SetPieChartCategoryId(slice.categoryId),
                                                )
                                                navigateToSubCategoryPieChart(isExpense)
                                            }
                                        }
                                    )
                                    HorizontalDivider(
                                        color = greyShade1,
                                        thickness = 1.sdp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.sdp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        NativeAd(
            context = context as Activity,
            adKey = AdKeys.PieChartNative.name,
            placementKey = if(isSubcategoryView) RemoteConfigKeys.PIE_CHART_NATIVE_ENABLED else RemoteConfigKeys.PIE_CHART_MAIN_NATIVE_ENABLED,
            layoutKey = if(isSubcategoryView) RemoteConfigKeys.PIE_CHART_NATIVE_LAYOUT else RemoteConfigKeys.PIE_CHART_MAIN_NATIVE_LAYOUT,
            screenName = if(isSubcategoryView) "PieChart_SubCategory_Screen_Bottom" else "PieChart_Category_Screen_Bottom" ,
            commonAdsUtil = commonAdsUtil
        )
    }
}
