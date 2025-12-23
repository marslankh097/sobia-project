package com.example.budgetly.ui.cash.accounts.screens.account_selection

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.NativeAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.domain.models.enums.common.Options
import com.example.budgetly.ui.banking.requisition.content.AccountsRow
import com.example.budgetly.ui.cash.accounts.AccountsViewModel
import com.example.budgetly.ui.cash.accounts.events.AccountEvent
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.events.TransactionEvent
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.dialog.UpdateDeleteDialog
import com.example.budgetly.utils.log
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun AccountSelectionScreen(modifier: Modifier = Modifier, navigateBack:()->Unit,
                           navigateToAccountInsertUpdate:()->Unit,
                           commonAdsUtil:CommonAdsUtil,
                           accountsViewModel: AccountsViewModel = hiltViewModel(),
                           transactionsViewModel: TransactionsViewModel = hiltViewModel(),
                           categoryViewModel: CategoryViewModel = hiltViewModel()
){
    val accountState by accountsViewModel.accountState.collectAsState()
//    val tempAccount by accountsViewModel.tempAccount.collectAsState()
    val context = LocalContext.current
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }
    Column(modifier.fillMaxSize()) {
        TopBar(
            title = "User Accounts",
            showApply = true,
            onButtonClick = {
                // Send selected ID back only on Apply
                log("tempAccount:${accountState.tempAccount}")
                accountsViewModel.onEvent(AccountEvent.SetSelectedAccount(accountState.tempAccount))
                categoryViewModel.onEvent(CategoryEvent.UpdateAccount(accountState.tempAccount?.accountId?:1))
                log("onButtonClick:${accountState.tempAccount}", "Account")
                transactionsViewModel.onEvent(TransactionEvent.SetDisplayAccount(accountState.tempAccount))
                handleBack.invoke()
            }
        ) {
            handleBack()
        }
        if(accountState.showDeleteConfirmationDialog){
            SimpleAlertDialog(
                modifier = Modifier.fillMaxWidth(),
                title = "Delete this Account!", msg = "Are you sure you want to delete this account?",
                positiveText = stringResource(R.string.delete),
                negativeText = stringResource(R.string.cancel),
                onButtonClick = {
                    accountsViewModel.onEvent(AccountEvent.ShowDeleteConfirmationDialog(false))
                    if(it){
                        accountsViewModel.onEvent(AccountEvent.DeleteByAccountId)
                    }
                }
            )
        }
        if(accountState.showUpdateDeleteDialog){
            UpdateDeleteDialog(title = "User Account",
                onOptionSelected = { selectedOption ->
                    accountsViewModel.onEvent(AccountEvent.ShowDeleteUpdateDialog(false))
                    if(selectedOption.isNotEmpty()){
                        if(selectedOption == Options.Delete.name){
                            accountsViewModel.onEvent(AccountEvent.ShowDeleteConfirmationDialog(true))
                        }else{
                            accountsViewModel.onEvent(AccountEvent.InsertUpdateMode(true))
                            navigateToAccountInsertUpdate()
                        }
                    }
                })
        }
        Column(modifier = Modifier.fillMaxWidth().weight(1f)){
            when {
                accountState.isLoading -> {
                    LoadingComponent(
                        textId = R.string.loading_accounts
                    )
                }

                accountState.error != null -> {
                    FailureComponent(
                        msg = accountState.error ?: "",
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
                            if (accountState.accountsList.isEmpty()) {
                                EmptyComponent(
                                    modifier = Modifier.fillMaxSize(),
                                    image = R.drawable.img_no_bank_accounts, text = R.string.no_bank_accounts_available
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(secondaryBgColor)
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.sdp, vertical = 12.sdp),
                                        contentPadding = PaddingValues(bottom = 60.sdp),
                                        verticalArrangement = Arrangement.spacedBy(10.sdp)
                                    ) {
                                        items(accountState.accountsList) { account ->
                                            val isSelected =
                                                account.accountId == accountState.tempAccount?.accountId
                                            AccountsRow(
                                                modifier = Modifier.fillMaxWidth(),
                                                titleHeading = stringResource(R.string.iban),
                                                title = account.accountIBAN,
                                                subTitleHeading = account.accountCategory.toSafeEmptyString(),
                                                subTitle = account.accountType.toSafeEmptyString(),
                                                isSelected = isSelected,
                                                onAccountSelected = {
                                                    accountsViewModel.onEvent(
                                                        AccountEvent.SetTempAccount(
                                                            account
                                                        )
                                                    )
                                                },
                                                onLongPressed = {
                                                    accountsViewModel.onEvent(AccountEvent.SetTargetAccount(
                                                        accountId = account.accountId,
                                                        accountType = account.accountType,
                                                        accountCategory = account.accountCategory,
                                                        accountIBAN = account.accountIBAN,
                                                        balance= account.initialBalance, currency = account.currency))
                                                    accountsViewModel.onEvent(AccountEvent.ShowDeleteUpdateDialog(true))
                                                }
                                            )
                                        }

                                    }
                                    Image(
                                        modifier = Modifier
                                            .size(70.sdp)
                                            .align(Alignment.BottomEnd)
                                            .padding(end = 30.sdp, bottom = 30.sdp)
                                            .safeClickAble {
                                                accountsViewModel.onEvent(AccountEvent.InsertUpdateMode(false))
                                                accountsViewModel.onEvent(AccountEvent.SetTargetAccount())
                                                navigateToAccountInsertUpdate()
                                            },
                                        painter = painterResource(R.drawable.icon_add_gradient),
                                        contentDescription = "Add New Account"
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
            adKey = AdKeys.AccountNative.name,
            placementKey = RemoteConfigKeys.ACCOUNT_NATIVE_ENABLED,
            layoutKey =  RemoteConfigKeys.ACCOUNT_NATIVE_LAYOUT,
            screenName =  "Account_Screen_Bottom" ,
            commonAdsUtil = commonAdsUtil
        )
    }
}