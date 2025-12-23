package com.example.budgetly.ui.banking.transactions
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ui.banking.transactions.content.SwitchTabsRow
import com.example.budgetly.ui.banking.transactions.content.TransactionListContent
import com.example.budgetly.ui.banking.transactions.events.TransactionEvent
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.defaultBgColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.TopBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

// 🧠 Composable Screens
// --- TransactionScreen.kt ---
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TransactionDisplayScreen(
    modifier: Modifier = Modifier,
    accountId: String,
    navigateBack:()->Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.onEvent(TransactionEvent.LoadAllTransactions(accountId))
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if(state.isLoading) defaultBgColor else secondaryBgColor)
    ) {
        TopBar(stringResource(R.string.account_transactions)
        ) {
            handleBack()
        }
        VerticalSpacer()
        SwitchTabsRow(selectedIndex = pagerState.currentPage, onClick = { index->
            viewModel.onEvent(TransactionEvent.ChangeTab(index))
            coroutineScope.launch { pagerState.animateScrollToPage(index)}})
        HorizontalPager(count = 3, state = pagerState) { page ->
            when (page) {
                0 -> TransactionListContent(
                    isLoading = state.isLoading,
                    transactions = state.allTransactions,
                    errorMessage = state.errorMessage
                ){viewModel.onEvent(TransactionEvent.LoadAllTransactions(accountId))}
                1 -> TransactionListContent(
                    isLoading = state.isLoading,
                    transactions = state.incomeTransactions,
                    errorMessage = state.errorMessage
//                ){viewModel.onEvent(TransactionEvent.LoadIncomeTransactions(accountId))}
                ){viewModel.onEvent(TransactionEvent.LoadAllTransactions(accountId))}
                2 -> TransactionListContent(
                    isLoading = state.isLoading,
                    transactions = state.expenseTransactions,
                    errorMessage = state.errorMessage
//                ){ viewModel.onEvent(TransactionEvent.LoadExpenseTransactions(accountId))}
                ){ viewModel.onEvent(TransactionEvent.LoadAllTransactions(accountId))}
            }
        }
    }
}