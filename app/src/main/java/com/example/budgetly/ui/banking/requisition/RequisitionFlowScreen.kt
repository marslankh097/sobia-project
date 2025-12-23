package com.example.budgetly.ui.banking.requisition

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ui.banking.requisition.content.AccountsRow
import com.example.budgetly.ui.banking.requisition.events.RequisitionEvent
import com.example.budgetly.ui.banking.requisition.state.RequisitionUiState
import com.example.budgetly.utils.defaultBgColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.delay

// --- COMPOSABLE SCREEN ---
@Composable
fun RequisitionFlowScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    fromIntent:Boolean = false,
    institutionId: String? = null,
    redirectUrl: String? = null,
    requisitionId: String?= null,
    onAccountClicked: ((String) -> Unit)? = null,
    navigateToAccounts:((String) -> Unit)?=null,
    viewModel: RequisitionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    BackHandler { navigateBack() }
    val handleBack = { navigateBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(defaultBgColor)
    ) {
        TopBar(if(fromIntent) "Account List" else "Create End User Agreement") {
            handleBack()
        }
        when (state) {
            is RequisitionUiState.Idle -> {
                if(fromIntent){
                    if (!requisitionId.isNullOrEmpty()) {
                        viewModel.onEvent(RequisitionEvent.FetchAccounts(requisitionId))
                    }
                }else{
                    if (!institutionId.isNullOrEmpty() && !redirectUrl.isNullOrEmpty()) {
                        viewModel.onEvent(RequisitionEvent.CreateRequisition(institutionId, redirectUrl))
                    }
                }
            }

            is RequisitionUiState.Loading -> {
                LoadingComponent(textId = if (institutionId != null) R.string.creating_end_user_agreement else R.string.fetching_accounts)
            }

            is RequisitionUiState.Success -> {
                val requisition = (state as RequisitionUiState.Success).requisition
                val isCached = (state as RequisitionUiState.Success).isCached
                LaunchedEffect(requisition.id,isCached) {
                    if(isCached){
                        navigateToAccounts?.invoke(requisition.id)
                    }else{
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(requisition.link)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                        delay(300)
                        navigateBack() // go back to wait for requisitionId trigger from MainActivity
                    }
                }
                if(isCached.not()){
                    LoadingComponent(textId = R.string.waiting_for_you_to_complete_in_browser)
                }
            }

            is RequisitionUiState.AccountList -> {
                val accounts = (state as RequisitionUiState.AccountList).accounts
                if (accounts.isEmpty()) {
                    EmptyComponent(
                        modifier = Modifier.fillMaxSize(),
                        image = R.drawable.img_no_bank_accounts, text =R.string.no_bank_accounts_available)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(color = secondaryBgColor)
                            .padding(horizontal = 12.sdp, vertical = 12.sdp),
                        contentPadding = PaddingValues(bottom = 12.sdp),
                        verticalArrangement = Arrangement.spacedBy(10.sdp)
                    ) {
                        items(accounts) { account ->
                            AccountsRow(
                                modifier = Modifier.fillMaxWidth(),
                                title = account.owner_name.toSafeEmptyString(),
                                subTitle = account.iban.toSafeEmptyString(),
                                onAccountSelected = {
                                    if (onAccountClicked != null) {
                                    onAccountClicked(account.id)
                                }}
                            )
                        }
                    }
                }
            }

            is RequisitionUiState.Error -> {
                FailureComponent(
                    msg = (state as RequisitionUiState.Error).message,
                    onTryAgainClick = {
                        if(fromIntent){
                            if (!requisitionId.isNullOrEmpty()) {
                                viewModel.onEvent(RequisitionEvent.FetchAccounts(requisitionId))
                            }
                        }else{
                            if (!institutionId.isNullOrEmpty() && !redirectUrl.isNullOrEmpty()) {
                                viewModel.onEvent(RequisitionEvent.CreateRequisition(institutionId, redirectUrl))
                            }
                        }
                    }
                )
            }
        }
    }
}

