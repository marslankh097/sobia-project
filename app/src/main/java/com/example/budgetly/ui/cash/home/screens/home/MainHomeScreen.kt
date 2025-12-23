package com.example.budgetly.ui.cash.home.screens.home
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.domain.models.enums.transaction.TransactionScreenSource
import com.example.budgetly.domain.models.enums.transaction.TransactionType
import com.example.budgetly.ui.cash.accounts.AccountsViewModel
import com.example.budgetly.ui.cash.accounts.events.AccountEvent
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.ui.cash.category.screens.category_insert.content.OptionSelectionDialog
import com.example.budgetly.ui.cash.filter.events.FilterEvent
import com.example.budgetly.ui.cash.home.screens.home.content.AccountAndDurationRow
import com.example.budgetly.ui.cash.home.screens.home.content.MainScreenContent
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.events.TransactionEvent
import com.example.budgetly.ui.cash.transaction.events.TransactionInsertEvent
import com.example.budgetly.utils.Utils
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.log
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.ImageSwitchRow
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toast
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainHomeScreen(
    modifier: Modifier = Modifier,
    commonAdsUtil: CommonAdsUtil,
    navigateBack: () -> Unit,
    navigateToAccountSelection: () -> Unit,
    onChartClick: (Boolean) -> Unit,
    navigateToTransactionDisplay: () -> Unit,
    navigateToFilterScreen: () -> Unit,
    navigateToCategoryDisplay:(Boolean)->Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val categoryState by categoryViewModel.state.collectAsState()
//    val transactionInsertState by transactionViewModel.insertState.collectAsState()
    val accountState by accountsViewModel.accountState.collectAsState()
    val transactionDisplayState by transactionViewModel.state.collectAsState()
    val filterState by transactionViewModel.filterState.collectAsState()
    val pieChartState by categoryViewModel.pieChartState.collectAsState()
    val handleBack = {
        if (pagerState.currentPage == 0) {
            navigateBack.invoke()
        } else {
            coroutineScope.launch {
                pagerState.scrollToPage(0)
            }
        }
    }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        categoryViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    context.toast(event.message)
                }
                else -> Unit
            }
        }
    }
    fun goToCategoryTransactions(category: CategoryModel?, subCategory: SubCategoryModel?, isExpense:Boolean){
        transactionViewModel.onEvent(TransactionEvent.SetDisplayCategorySubCategoryAccount(category,subCategory, accountState.selectedAccount))
        transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionType(if(isExpense) TransactionType.Expense.name else TransactionType.Income.name))
        transactionViewModel.onEvent(FilterEvent.SetScreenSource(TransactionScreenSource.Category.name))
        navigateToTransactionDisplay()

    }
    fun goToAllTransactions(isExpense:Boolean){
        val category = if(isExpense) categoryState.expenseCategories.firstOrNull() else categoryState.incomeCategories.firstOrNull()
        val subCategory = categoryState.subCategoriesMap[category?.categoryId]?.firstOrNull()
        transactionViewModel.onEvent(TransactionEvent.SetDisplayCategorySubCategoryAccount(category,subCategory, accountState.selectedAccount))
        transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionType(if(isExpense) TransactionType.Expense.name else TransactionType.Income.name))
        transactionViewModel.onEvent(FilterEvent.SetScreenSource( if(isExpense) TransactionScreenSource.Expense.name else TransactionScreenSource.Income.name))
        navigateToTransactionDisplay()
    }
    BackHandler { handleBack() }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TopBar(stringResource(if (pagerState.currentPage == 0) R.string.expense  else R.string.income), isActivity = false) {
            handleBack()
        }
        if(filterState.showDurationSelectionDialog){
            OptionSelectionDialog(modifier = Modifier.fillMaxWidth(),
                title = "Select Past Transactions For",
                options = TransactionDuration.entries.map { stringResource(it.title) },
                selectedOption = stringResource(filterState.transactionDuration.title),
                onOptionSelected = {selectedDuration->
                    transactionViewModel.onEvent(FilterEvent.ShowDurationSelectionDialog(false))
                    if(selectedDuration.isNotEmpty()){
                        val duration = TransactionDuration.entries.find { context.getString(it.title)  == selectedDuration}
                        duration?.let {
                            if(duration == TransactionDuration.DateRange){
                                transactionViewModel.onEvent(FilterEvent.UseCustomDateRange(true))
                                transactionViewModel.onEvent(FilterEvent.SetTempFromTimeStamp(filterState.fromTimeStamp))
                                transactionViewModel.onEvent(FilterEvent.SetTempToTimeStamp(filterState.toTimeStamp))
                                navigateToFilterScreen()
                            }else{
                                val (from, to) = Utils.getTimeStampsFromDays(duration.days)
                                transactionViewModel.onEvent(FilterEvent.UseCustomDateRange(false))
                                transactionViewModel.onEvent(FilterEvent.SetTransactionDuration(it))
                                transactionViewModel.onEvent(FilterEvent.SetTimeStamp(from, to))
                                categoryViewModel.onEvent(CategoryEvent.SetTimeStamp(from,to))
                            }
                        }

                    }
                })
        }
        when {
            categoryState.isLoading -> {
                LoadingComponent(
                    textId = if (pagerState.currentPage == 0) R.string.loading_expense_categories else R.string.loading_income_categories
                )
            }

            categoryState.error != null -> {
                FailureComponent(
                    msg = categoryState.error!!,
                    onTryAgainClick = { categoryViewModel.onEvent(CategoryEvent.LoadCategories) }
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = secondaryBgColor), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalSpacer(8)
                    log("MainHomeScreen:selectedAccount: ${accountState.selectedAccount}")
                    AccountAndDurationRow(
                        accountTitle = accountState.selectedAccount?.accountType ?: "Cash",
                        accountCurrency = accountState.selectedAccount?.currency ?: "PKR",
                        durationTitle = stringResource(filterState.transactionDuration.title),
                        onAccountClick = {
                            accountsViewModel.onEvent(AccountEvent.SetTempAccount(accountState.selectedAccount))
                            navigateToAccountSelection()
                        },
                        onDurationClick = {
                            transactionViewModel.onEvent(FilterEvent.ShowDurationSelectionDialog(true))
                        }
                    )
                    VerticalSpacer(8)
                    ImageSwitchRow(
                        imageId1 = R.drawable.icon_expense,
                        imageId2 = R.drawable.icon_income,
                        tabText1 = stringResource(R.string.expense),
                        tabText2 = stringResource(R.string.income),
                        switchOn = pagerState.currentPage == 1,
                        switchChanged = {
                            log("switchChanged:$it")
                            coroutineScope.launch {
                                pagerState.scrollToPage(if (it) 1 else 0)
                                log("pagerState:${pagerState.currentPage}")
                            }
                        }
                    )
                    VerticalSpacer(8)
                    HorizontalPager(count = 2, state = pagerState) { page ->
                        val isExpense = page == 0
                        MainScreenContent(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            commonAdsUtil = commonAdsUtil,
                            isExpense = isExpense,
                            categoryState = categoryState,
                            pieChartState = pieChartState,
                            transactionDisplayState = transactionDisplayState,
                            navigateToCategoryDisplay = { navigateToCategoryDisplay(isExpense) },
                            onCategoryCardClick = { category, subCategory ->
                                goToCategoryTransactions(category, subCategory, isExpense)
                            },
                            onMainCardClick = { goToAllTransactions(isExpense) },
                            onChartClick = {
                                categoryViewModel.onEvent(CategoryEvent.LoadCategoryPieChartDetail(isExpense))
                                onChartClick(isExpense)
                                           },
                            tryAgainClick = { fromCategories ->
                                if (fromCategories) categoryViewModel.onEvent(CategoryEvent.LoadCategories)
                                else categoryViewModel.onEvent(CategoryEvent.LoadPieChart)
                            }
                        )
                    }
                }
            }
        }
    }
}