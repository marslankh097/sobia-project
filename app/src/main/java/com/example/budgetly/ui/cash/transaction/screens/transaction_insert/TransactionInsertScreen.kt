package com.example.budgetly.ui.cash.transaction.screens.transaction_insert
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency
import com.example.budgetly.domain.models.enums.transaction.TransactionScreenSource
import com.example.budgetly.domain.models.enums.transaction.TransactionType
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.ui.cash.category.screens.category_insert.content.CustomSelectionRow
import com.example.budgetly.ui.cash.category.screens.category_insert.content.OptionSelectionDialog
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.events.TransactionInsertEvent
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.Utils
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.log
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.CustomTextField
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toDateString
import com.example.budgetly.utils.toast
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun TransactionInsertScreen(modifier: Modifier = Modifier,
                            navigateBack:()->Unit,
                            navigateToCurrencySelection:()->Unit,
                            navigateToCategorySelection:(Boolean)->Unit,
                            navigateToAccountSelection:()->Unit,
                            navigateToDateTimePicker:(Long)->Unit,
                            navController: NavController,
                            transactionViewModel:TransactionsViewModel = hiltViewModel(),
                            categoryViewModel: CategoryViewModel = hiltViewModel()
){
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }
    val context = LocalContext.current
    val insertState by transactionViewModel.insertState.collectAsState()
    val transactionState by transactionViewModel.state.collectAsState()
    val filterState by transactionViewModel.filterState.collectAsState()
    val isExpense = insertState.type == TransactionType.Expense.name
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Long>("selectedDateTime")?.observe(lifecycleOwner) { newTime ->
            log("newTime: ${newTime.toDateString()}")
            transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionDateTime(newTime))
        }
    }

    LaunchedEffect(Unit) {
        transactionViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.Saved -> {
                    handleBack()
                }
                is UiEvent.ShowToast -> {
                    context.toast(event.message)
                }
                else -> Unit
            }
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        if(insertState.showFrequencySelectionDialog){
            OptionSelectionDialog(modifier = Modifier.fillMaxWidth(),
                title = "Select Transaction Frequency!",
                options = TransactionFrequency.entries.map { it.name },
                selectedOption = insertState.frequency, onOptionSelected = {selectedFrequency->
                    transactionViewModel.onEvent(TransactionInsertEvent.ShowFrequencySelectionDialog(false))
                    if(selectedFrequency.isNotEmpty()){
                        transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionFrequency(selectedFrequency))
                    }
                })
        }
        val text = if (isExpense) "Expenses" else "Income"
        val toolbarTitle = when(TransactionScreenSource.valueOf(filterState.screenSource)){
            TransactionScreenSource.All -> "All Transactions"
            TransactionScreenSource.Category -> "${insertState.categoryModel?.categoryName?: "$text Transactions"} $text"
            TransactionScreenSource.SubCategory -> "${insertState.subCategoryModel?.subCategoryName?: "$text Transactions"} $text"
            TransactionScreenSource.Expense -> "All Expense Transactions"
            TransactionScreenSource.Income -> "All Income Transactions"
        }
        TopBar(title = toolbarTitle, onClick = handleBack)
        Column(
            Modifier
                .fillMaxWidth()
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            Text( text =
                if(isExpense) stringResource(R.string.enter_expense_amount) else stringResource(R.string.enter_income_amount)
                , style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                text = insertState.amount,
                inputType = KeyboardType.Decimal,
                hint = if(isExpense) stringResource(R.string.enter_expense_amount_here) else stringResource(R.string.enter_income_amount_here),
                onValueChange = { transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionAmount(it)) },
                iconCrossClick = { transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionAmount("")) }
            )
            VerticalSpacer(15)
            Text("Select  Currency:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomSelectionRow(
                modifier = Modifier.fillMaxWidth(),
                title = insertState.currency,
                isExpanded = false,
                onCategorySelected = {
                    transactionViewModel.onEvent(TransactionInsertEvent.SetTempCurrency(currency = insertState.currency))
                    navigateToCurrencySelection()
                }
            )
            if(insertState.displayCategorySelection){
                VerticalSpacer(15)
                Text("Select  Category:", style = SubtitleMedium, color = primaryColor)
                VerticalSpacer(12)
                CustomSelectionRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = insertState.categoryModel?.categoryName?:"None",
                    isExpanded = false,
                    onCategorySelected = {
                        transactionViewModel.onEvent(TransactionInsertEvent.SetTempCategory(category = insertState.categoryModel))
                        transactionViewModel.onEvent(TransactionInsertEvent.SetSelectCategory(true))
                        navigateToCategorySelection(isExpense)
                    }
                )
            }
            if(insertState.displaySubCategorySelection){
                VerticalSpacer(15)
                Text("Select SubCategory:", style = SubtitleMedium, color = primaryColor)
                VerticalSpacer(12)
                CustomSelectionRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = insertState.subCategoryModel?.subCategoryName?:"None",
                    isExpanded = false,
                    onCategorySelected = {
                        transactionViewModel.onEvent(TransactionInsertEvent.SetTempSubCategory(subCategory = insertState.subCategoryModel))
                        transactionViewModel.onEvent(TransactionInsertEvent.SetSelectCategory(false))
                        navigateToCategorySelection(isExpense)
                    }
                )
            }
            if(insertState.displayAccountSelection){
                VerticalSpacer(15)
                Text("Select Account:", style = SubtitleMedium, color = primaryColor)
                VerticalSpacer(12)
                CustomSelectionRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = insertState.accountModel?.accountType?:"Cash",
                    isExpanded = false,
                    onCategorySelected = {
                        transactionViewModel.onEvent(TransactionInsertEvent.SetTempAccount(account = insertState.accountModel))
                        navigateToAccountSelection()
                    }
                )
            }
            VerticalSpacer(15)
            Text("Select Transaction Date & Time:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomSelectionRow(
                modifier = Modifier.fillMaxWidth(),
                title = insertState.dateTime.toDateString("dd MMM yyyy, hh: mm: ss"),
                isExpanded = false,
                onCategorySelected = {
                    // Navigate to your custom date time picker screen or show dialog
                    navigateToDateTimePicker(insertState.dateTime)
                }
            )
            VerticalSpacer(15)
            Text("Select Transaction Frequency:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomSelectionRow(
                modifier = Modifier.fillMaxWidth(),
                title = insertState.frequency,
                isExpanded = false,
                onCategorySelected = {
                    transactionViewModel.onEvent(TransactionInsertEvent.ShowFrequencySelectionDialog(true))
                }
            )
            VerticalSpacer(15)
            Text("Enter Transaction Notes:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                text = insertState.notes,
                hint = "Enter Transaction Notes here",
                onValueChange = { transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionNotes(it)) },
                iconCrossClick = { transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionNotes("")) }
            )
            VerticalSpacer(20)
            PrimaryButtonRounded(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(secondaryColor, secondaryColor),
                isRound = false,
                onButtonClick = {
                    // add or update a transaction
                    transactionViewModel.onEvent(TransactionInsertEvent.InsertOrUpdateTransaction)
                    val duration  = TransactionDuration.entries.find { it.name == categoryViewModel.getSelectedDuration() }
                    duration?.let {
                        val (from, to) = Utils.getTimeStampsFromDays(duration.days)
                        categoryViewModel.onEvent(CategoryEvent.SetTimeStamp(from,to))
                    }
                }
            ) {
                Text(if (insertState.isUpdateMode) "Update" else "Add", style = SubtitleLarge, color = white)
            }
        }
    }
}