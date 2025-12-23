package com.example.budgetly.ui.upload_receipt.screens.receipt_history

import android.app.Activity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.NativeAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.upload_receipt.ReceiptViewModel
import com.example.budgetly.ui.upload_receipt.events.ReceiptEvent
import com.example.budgetly.ui.upload_receipt.screens.receipt_history.content.ReceiptHistoryRow
import com.example.budgetly.ui.upload_receipt.state.ReceiptUiState
import com.example.budgetly.ui.upload_receipt.state.receiptsOrEmpty
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import ir.kaaveh.sdpcompose.sdp


@Composable
fun ReceiptHistoryScreen(
    modifier: Modifier = Modifier,
    receiptViewModel: ReceiptViewModel = hiltViewModel(),
    commonAdsUtil: CommonAdsUtil,
    navigateToReceiptResult: () -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state by receiptViewModel.receiptState.collectAsState()
    val handleBack = { navigateBack() }

    BackHandler { handleBack() }

    // ─────── Delete Confirmation Dialog ───────
    if (state.showDeleteReceiptDialog || state.showDeleteAllDialog) {
        SimpleAlertDialog(
            title = if (state.showDeleteReceiptDialog)
                stringResource(R.string.discard_receipt)
            else
                stringResource(R.string.discard_all_receipts),
            msg = if (state.showDeleteReceiptDialog)
                stringResource(R.string.are_you_sure_you_want_to_discard_this_receipt)
            else
                stringResource(R.string.are_you_sure_you_want_to_discard_all_receipts_from_your_history),
            positiveText = stringResource(R.string.delete),
            negativeText = stringResource(R.string.cancel)
        ) { confirmed ->
            if (confirmed) {
                if (state.showDeleteReceiptDialog) {
                    receiptViewModel.onEvent(ReceiptEvent.DeleteReceipt)
                } else {
                    receiptViewModel.onEvent(ReceiptEvent.DeleteAllReceipts)
                }
            }
            receiptViewModel.onEvent(ReceiptEvent.ShowDeleteReceiptDialog(false))
            receiptViewModel.onEvent(ReceiptEvent.ShowDeleteAllDialog(false))
        }
    }

    // ─────── UI Layout ───────
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(secondaryBgColor)
    ) {
        // Top Bar with Delete Icon
        TopBar(
            title = stringResource(R.string.receipt_history),
            icon3 = if (state.uiState.receiptsOrEmpty().isNotEmpty()) R.drawable.img_delete else null,
            onClickIcon3 = if (state.uiState.receiptsOrEmpty().isNotEmpty()) {
                { receiptViewModel.onEvent(ReceiptEvent.ShowDeleteAllDialog(true)) }
            } else null,
            onClick = { handleBack() }
        )
        Column(modifier = Modifier.weight(1f)){
            when (val uiState = state.uiState) {
                is ReceiptUiState.Loading -> {
                    LoadingComponent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.sdp),
                        textId = R.string.fetching_receipts

                    )
                }

                is ReceiptUiState.Error -> {
                    FailureComponent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.sdp),
                        msg = uiState.message,
                        onTryAgainClick = {
                            receiptViewModel.onEvent(ReceiptEvent.ObserveAllReceipts)
                        }
                    )
                }

                is ReceiptUiState.Success -> {
                    val receipts = uiState.receipts

                    if (receipts.isEmpty()) {
                        EmptyComponent(
                            modifier = Modifier.fillMaxSize(),
                            image = R.drawable.img_shopping,
                            text = R.string.no_processed_receipts_available_yet
                        )
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
                            items(receipts) { receipt ->
                                ReceiptHistoryRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    iconId = R.drawable.img_shop,
                                    title = receipt.vendor.ifEmpty { "None" },
                                    itemCount = receipt.items.size.toString(),
                                    subTitle = receipt.date.ifEmpty { "N/A" },
                                    onDeleteClicked = {
                                        receiptViewModel.onEvent(ReceiptEvent.SetTempReceiptId(receipt.receiptId))
                                        receiptViewModel.onEvent(ReceiptEvent.ShowDeleteReceiptDialog(true))
                                    },
                                    onReceiptSelected = {
                                        receiptViewModel.onEvent(ReceiptEvent.SelectReceipt(receipt))
                                        navigateToReceiptResult()
                                    }
                                )
                            }
                        }
                        NativeAd(
                            context = context as Activity,
                            adKey = AdKeys.ReceiptNative.name,
                            placementKey = RemoteConfigKeys.RECEIPT_HISTORY_NATIVE_ENABLED,
                            layoutKey = RemoteConfigKeys.RECEIPT_HISTORY_NATIVE_LAYOUT,
                            screenName = "Receipt_Result_Screen_Bottom",
                            commonAdsUtil = commonAdsUtil
                        )
                    }
                }

                ReceiptUiState.Idle -> {
                    EmptyComponent(
                        modifier = Modifier.fillMaxSize(),
                        image = R.drawable.img_shopping,
                        text = R.string.no_processed_receipts_available_yet
                    )
                }
            }
        }
    }
}

/*
@Composable
fun ReceiptHistoryScreen(
    modifier: Modifier = Modifier,
    receiptViewModel: ReceiptViewModel = hiltViewModel(),
    navigateToReceiptResult: () -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val receiptState by receiptViewModel.receiptState.collectAsState()
    val handleBack = {
        log("inside.handleBack")
        navigateBack()
    }
    BackHandler {
        handleBack()
    }
    if(receiptState.showDeleteReceiptDialog || receiptState.showDeleteAllDialog){
        SimpleAlertDialog(
            title = if(receiptState.showDeleteReceiptDialog) {
                stringResource(R.string.delete_receipt)
            }else{
                stringResource(R.string.delete_all_receipts)
            },
            msg = if(receiptState.showDeleteReceiptDialog) {
                stringResource(R.string.are_you_sure_you_want_to_delete_this_receipt)
            }else{
                stringResource(R.string.are_you_sure_you_want_to_delete_all_receipts_from_your_history)
            }, positiveText = stringResource(R.string.delete), negativeText = stringResource(R.string.cancel)
        ) {
            if(it){
                if(receiptState.showDeleteReceiptDialog) {
                    receiptViewModel.onEvent(ReceiptEvent.DeleteReceipt)
                }else{
                    receiptViewModel.onEvent(ReceiptEvent.DeleteAllReceipts)
                }
            }
            if(receiptState.showDeleteReceiptDialog) {
                receiptViewModel.onEvent(ReceiptEvent.ShowDeleteReceiptDialog(false))
            }else{
                receiptViewModel.onEvent(ReceiptEvent.ShowDeleteAllDialog(false))
            }
        }
    }
    Column(modifier = modifier
        .fillMaxSize()
        .background(secondaryBgColor)) {
        TopBar(title = "Receipt History", icon3 = if(receiptState.receipts.isNotEmpty() )R.drawable.img_delete else null, onClickIcon3 =
            if(receiptState.receipts.isNotEmpty()){ {
                    //delete all receipts click
                    receiptViewModel.onEvent(ReceiptEvent.ShowDeleteAllDialog(true))}
            }
            else null
        ) {
            handleBack()
        }
        if (receiptState.receipts.isEmpty()) {
            EmptyComponent(
                modifier = Modifier.fillMaxSize(),
                image = R.drawable.img_shopping, text = R.string.no_processed_receipts_available_yet
            )
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
                items(receiptState.receipts) { receipt ->
                    ReceiptHistoryRow(
                        modifier = Modifier.fillMaxWidth(),
                        iconId = R.drawable.img_shop,
                        title = receipt.vendor.ifEmpty { "None" },
                        itemCount = receipt.items.count().toString(),
                        subTitle = receipt.date.ifEmpty { "N/A" },
                        onDeleteClicked = {
                            receiptViewModel.onEvent(ReceiptEvent.SetTempReceiptId(receipt.receiptId))
                            receiptViewModel.onEvent(ReceiptEvent.ShowDeleteReceiptDialog(true))
                        }, onReceiptSelected = {
                            receiptViewModel.onEvent(ReceiptEvent.SelectReceipt(receipt))
                            navigateToReceiptResult()
                        }
                    )
                  }
            }
        }
    }
}*/
