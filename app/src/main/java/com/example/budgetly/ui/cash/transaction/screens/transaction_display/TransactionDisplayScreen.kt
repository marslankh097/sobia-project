package com.example.budgetly.ui.cash.transaction.screens.transaction_display

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.domain.models.enums.transaction.TransactionScreenSource
import com.example.budgetly.domain.models.enums.transaction.TransactionType
import com.example.budgetly.ui.cash.accounts.AccountsViewModel
import com.example.budgetly.ui.cash.accounts.events.AccountEvent
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.screens.category_insert.content.OptionSelectionDialog
import com.example.budgetly.ui.cash.filter.events.FilterEvent
import com.example.budgetly.ui.cash.home.screens.home.content.AccountAndDurationRow
import com.example.budgetly.ui.cash.sort.content.SortBottomSheetDialog
import com.example.budgetly.ui.cash.sort.events.SortEvent
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.events.GraphEvent
import com.example.budgetly.ui.cash.transaction.events.TransactionEvent
import com.example.budgetly.ui.cash.transaction.screens.transaction_display.content.TransactionsGraphPage
import com.example.budgetly.ui.cash.transaction.screens.transaction_display.content.TransactionsListPage
import com.example.budgetly.utils.Utils
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.log
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.ImageSwitchRow
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toSafeDouble
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TransactionsDisplayScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToTransactionInsertUpdate: () -> Unit,
    navigateToAccountSelection: () -> Unit,
    navigateToFilterScreen: () -> Unit,
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    val transactionState by transactionViewModel.state.collectAsState()
    val filterState by transactionViewModel.filterState.collectAsState()
    val graphState by transactionViewModel.graphState.collectAsState()
    val insertState by transactionViewModel.insertState.collectAsState()
    val sortState by transactionViewModel.sortState.collectAsState()

    log("transactionState.account: ${transactionState.accountModel}")

    val isExpense = insertState.type == TransactionType.Expense.name
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val handleBack = {
        if(pagerState.currentPage == 0){
            navigateBack()
        }else{
            coroutineScope.launch { pagerState.scrollToPage(0) }
        }
    }
    BackHandler { handleBack() }
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
    if(sortState.showSortingDialog){
        SortBottomSheetDialog(modifier = Modifier.fillMaxWidth(),
            currentSortBy = sortState.tempSortBy,
            currentOrderBy = sortState.tempOrderBy,
            onSortBySelected = {
               transactionViewModel.onEvent(SortEvent.SetTempSortBy(it))
            },
            onOrderBySelected = {
                transactionViewModel.onEvent(SortEvent.SetTempOrderBy(it))
            },
            onButtonClick = {
                if(it){
                    transactionViewModel.onEvent(SortEvent.SetSortBy(sortState.tempSortBy))
                    transactionViewModel.onEvent(SortEvent.SetOrderBy(sortState.tempOrderBy))
                }
                transactionViewModel.onEvent(SortEvent.ShowSortingDialog(false))
            })
    }
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val toolbarTitle = when (TransactionScreenSource.valueOf(filterState.screenSource)) {
            TransactionScreenSource.All -> stringResource(R.string.all_transactions)
            TransactionScreenSource.Category -> "${transactionState.categoryModel?.categoryName.orEmpty()} ${if (isExpense) stringResource(
                R.string.expenses
            ) else stringResource(
                R.string.income
            )}"
            TransactionScreenSource.SubCategory -> "${transactionState.subCategoryModel?.subCategoryName.orEmpty()} ${if (isExpense) stringResource(
                R.string.expenses
            ) else stringResource(
                R.string.income
            )}"
            TransactionScreenSource.Expense -> stringResource(R.string.all_expense_transactions)
            TransactionScreenSource.Income -> stringResource(R.string.all_income_transactions)
        }
        TopBar(title = toolbarTitle, onClick = {handleBack()},
            icon1 = R.drawable.icon_filter, icon2 = R.drawable.icon_sort,
            onClickIcon1 = {
                // filter click
                transactionViewModel.onEvent(FilterEvent.UseCustomDateRange(filterState.transactionDuration == TransactionDuration.DateRange))
                transactionViewModel.onEvent(FilterEvent.SetTempFromTimeStamp(filterState.fromTimeStamp))
                transactionViewModel.onEvent(FilterEvent.SetTempToTimeStamp(filterState.toTimeStamp))
                navigateToFilterScreen()
            },
            onClickIcon2 = {
                //sort click
                transactionViewModel.onEvent(SortEvent.SetTempSortBy(sortState.sortBy))
                transactionViewModel.onEvent(SortEvent.SetTempOrderBy(sortState.orderBy))
                transactionViewModel.onEvent(SortEvent.ShowSortingDialog(true))
            }
            )

        // 🟡 Handle loading and error first:
        when {
            transactionState.isLoading || graphState.isLoading -> {
                LoadingComponent(textId = R.string.fetching_transactions)
            }
            transactionState.error != null || graphState.error != null -> {
                FailureComponent(
                    msg = transactionState.error!!,
                    onTryAgainClick = { transactionViewModel.onEvent(TransactionEvent.LoadTransactions) }
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = secondaryBgColor),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VerticalSpacer(8)
                    AccountAndDurationRow(
                        accountTitle = transactionState.accountModel?.accountType ?: "Cash",
                        accountCurrency = transactionState.accountModel?.currency ?: "PKR",
                        durationTitle = stringResource(filterState.transactionDuration.title),
                        onAccountClick = {
                            accountsViewModel.onEvent(AccountEvent.SetTempAccount(transactionState.accountModel))
                            navigateToAccountSelection()
                        },
                        onDurationClick = {
                            transactionViewModel.onEvent(FilterEvent.ShowDurationSelectionDialog(true))
                        }
                    )
                    // ✅ Success: show pager & switch
                    VerticalSpacer()
                    ImageSwitchRow(
                        imageId1 = R.drawable.icon_list,
                        imageId2 = R.drawable.icon_graph,
                        tabText1 = stringResource(R.string.transactions),
                        tabText2 = stringResource(R.string.graph),
                        switchOn = pagerState.currentPage == 1,
                        switchChanged = {
                            coroutineScope.launch { pagerState.scrollToPage(if (it) 1 else 0) }
                        }
                    )
                    if(pagerState.currentPage == 1) VerticalSpacer()
                    HorizontalPager(
                        count = 2,
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        if (page == 0) {
                            TransactionsListPage(
                                transactionState = transactionState,
                                isExpense = isExpense,
                                onTransactionViewModelEvent = transactionViewModel::onEvent,
                                onTransactionViewModelInsertEvent = transactionViewModel::onEvent,
                                navigateToTransactionInsertUpdate = navigateToTransactionInsertUpdate
                            )
                        } else {
                            TransactionsGraphPage(
                                transactionTotal = transactionState.transactions.sumOf { it.amount.toSafeDouble() }.toString() ,
                                graphState = graphState,
                                onTryAgain = {
                                    transactionViewModel.onEvent(GraphEvent.LoadTransactionGraph)
                                },
                                changeGraphType = {
                                    transactionViewModel.onEvent(GraphEvent.ChangeGraphType)
                                },
                                transactionDisplayState = transactionState ,
                            )
                        }
                    }
                }
            }

        }
    }
}
