package com.example.budgetly.ui.cash.accounts.screens.account_insert

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.enums.account.AccountCategory
import com.example.budgetly.domain.models.enums.account.AccountType
import com.example.budgetly.ui.cash.accounts.AccountsViewModel
import com.example.budgetly.ui.cash.accounts.events.AccountEvent
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.ui.cash.category.screens.category_insert.content.CustomSelectionRow
import com.example.budgetly.ui.cash.category.screens.category_insert.content.OptionSelectionDialog
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.events.TransactionEvent
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.log
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.CustomTextField
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toast
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun AccountInsertUpdateScreen(modifier: Modifier = Modifier,
                              navigateToCurrencySelection:()-> Unit,
                              navigateBack:()->Unit,
                              accountsViewModel: AccountsViewModel = hiltViewModel(),
                              categoryViewModel: CategoryViewModel = hiltViewModel(),
                              transactionsViewModel: TransactionsViewModel = hiltViewModel(),
                              ){
    val insertState by accountsViewModel.insertState.collectAsState()
    val accountState by accountsViewModel.accountState.collectAsState()
    val context = LocalContext.current
    val handleBack = {navigateBack()}
    BackHandler { handleBack() }
    // Listen for success event
    LaunchedEffect(Unit) {
        accountsViewModel.eventFlow.collect { event ->
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
        TopBar(if(insertState.isUpdateMode) "Update Account" else "Add Account"){ navigateBack() }
        if(insertState.showAccountTypeSelectionDialog){
            OptionSelectionDialog(modifier = Modifier.fillMaxWidth(),
                title = "Select Account Type:",
                options = AccountType.entries.map { it.name },
                selectedOption = insertState.accountType, onOptionSelected = {selectedAccountType->
                    accountsViewModel.onEvent(AccountEvent.ShowAccountTypeSelectionDialog(false))
                    if(selectedAccountType.isNotEmpty()){
                        accountsViewModel.onEvent(AccountEvent.SetAccountType(selectedAccountType))
                    }
                })
        }
        if(insertState.showAccountCategorySelectionDialog){
            OptionSelectionDialog(modifier = Modifier.fillMaxWidth(),
                title = "Select Account Type:",
                options = AccountCategory.entries.map { it.name },
                selectedOption = insertState.accountCategory, onOptionSelected = {selectedAccountCategory->
                    accountsViewModel.onEvent(AccountEvent.ShowAccountCategorySelectionDialog(false))
                    if(selectedAccountCategory.isNotEmpty()){
                        accountsViewModel.onEvent(AccountEvent.SetAccountCategory(selectedAccountCategory))
                    }
                })
        }
        Column(
            Modifier
                .fillMaxWidth()
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            Text( "Enter Account IBAN:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                text = insertState.accountIBAN,
                hint = "Enter Account IBAN here",
                onValueChange = { accountsViewModel.onEvent(AccountEvent.SetAccountIBAN(it)) },
                iconCrossClick = { accountsViewModel.onEvent(AccountEvent.SetAccountIBAN("")) }
            )
//            if(insertState.isUpdateMode.not()){
                VerticalSpacer(15)
                Text( text = "Enter Initial Balance"
                    , style = SubtitleMedium, color = primaryColor)
                VerticalSpacer(12)
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    text = insertState.balance,
                    inputType = KeyboardType.Decimal,
                    hint = "Enter Account Balance here",
                    onValueChange = { accountsViewModel.onEvent(AccountEvent.SetAccountBalance(it)) },
                    iconCrossClick = { accountsViewModel.onEvent(AccountEvent.SetAccountBalance("")) }
                )
//            }
            VerticalSpacer(15)
            Text("Select  Currency:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomSelectionRow(
                modifier = Modifier.fillMaxWidth(),
                title = insertState.currency,
                isExpanded = false,
                onCategorySelected = {
                    accountsViewModel.onEvent(AccountEvent.SetTempCurrency(currency = insertState.currency))
                    navigateToCurrencySelection()
                }
            )
            VerticalSpacer(15)
            Text("Select Account Type:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomSelectionRow(
                modifier = Modifier.fillMaxWidth(),
                title = insertState.accountType,
                isExpanded = false,
                onCategorySelected = {
                    accountsViewModel.onEvent(AccountEvent.ShowAccountTypeSelectionDialog(true))
                }
            )
            VerticalSpacer(15)
            Text("Select Account Category:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomSelectionRow(
                modifier = Modifier.fillMaxWidth(),
                title = insertState.accountCategory,
                isExpanded = false,
                onCategorySelected = {
                    accountsViewModel.onEvent(AccountEvent.ShowAccountCategorySelectionDialog(true))
                }
            )
            VerticalSpacer(20)
            PrimaryButtonRounded(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(secondaryColor, secondaryColor),
                isRound = false,
                onButtonClick = {
                    val account = AccountModel(
                        accountId = insertState.accountId ?: 0L,
                        accountIBAN = insertState.accountIBAN,
                        currency = insertState.currency,
//                        initialBalance = if (!insertState.isUpdateMode) insertState.balance else transactionsViewModel.insertState.value.accountModel?.initialBalance ?: "0.0",
                        initialBalance = insertState.balance,
                        balance = if (!insertState.isUpdateMode) insertState.balance else transactionsViewModel.state.value.accountModel?.balance ?: "0.0",
                        accountType = insertState.accountType,
                        accountCategory = insertState.accountCategory,
                    )
                    accountsViewModel.onEvent(AccountEvent.InsertOrUpdateAccount(account))
                    if(insertState.isUpdateMode){
                        if(insertState.accountId == accountState.selectedAccount?.accountId){
                            accountsViewModel.onEvent(AccountEvent.SetSelectedAccount(account))
                            accountsViewModel.onEvent(AccountEvent.SetTempAccount(account))
                            categoryViewModel.onEvent(CategoryEvent.UpdateAccount(account.accountId))
                            log("SetDisplayAccount:$account", "Account")
                            transactionsViewModel.onEvent(TransactionEvent.SetDisplayAccount(account = account))
                        }
                    }
                }
            ) {
                Text(if (insertState.isUpdateMode) "Update" else "Add", style = SubtitleLarge, color = white)
            }
        }
    }
}