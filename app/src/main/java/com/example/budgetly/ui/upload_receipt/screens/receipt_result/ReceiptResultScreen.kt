package com.example.budgetly.ui.upload_receipt.screens.receipt_result

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.budgetly.ui.upload_receipt.screens.receipt_result.content.ReceiptResultRow
import com.example.budgetly.ui.upload_receipt.state.ReceiptUiState
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import ir.kaaveh.sdpcompose.sdp


@Composable
fun ReceiptResultScreen(
    modifier: Modifier = Modifier,
    receiptViewModel: ReceiptViewModel = hiltViewModel(),
    commonAdsUtil: CommonAdsUtil,
    navigateBack: () -> Unit
) {
    val state by receiptViewModel.receiptState.collectAsState()
    val expandedReceipts by receiptViewModel.expandedReceipts.collectAsState()
    val expandedReceiptItems by receiptViewModel.expandedReceiptItems.collectAsState()
    val context = LocalContext.current

    val handleBack = { navigateBack() }

    BackHandler { handleBack() }

    // ─────── Alert Dialog ───────
    if (state.showDeleteReceiptDialog || state.showDeleteItemDialog) {
        SimpleAlertDialog(
            title = if (state.showDeleteReceiptDialog)
                stringResource(R.string.discard_receipt)
            else
                stringResource(R.string.discard_receipt_item),
            msg = if (state.showDeleteReceiptDialog)
                stringResource(R.string.are_you_sure_you_want_to_discard_this_receipt)
            else
                stringResource(R.string.are_you_sure_you_want_to_discard_this_receipt_item),
            positiveText = stringResource(R.string.discard),
            negativeText = stringResource(R.string.cancel)
        ) { confirmed ->
            if (confirmed) {
                if (state.showDeleteReceiptDialog) {
                    receiptViewModel.onEvent(ReceiptEvent.DeleteReceipt)
                    handleBack()
                } else {
                    receiptViewModel.onEvent(ReceiptEvent.DeleteItem)
                }
            }
            receiptViewModel.onEvent(
                if (state.showDeleteReceiptDialog)
                    ReceiptEvent.ShowDeleteReceiptDialog(false)
                else
                    ReceiptEvent.ShowDeleteItemDialog(false)
            )
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(R.string.receipt_results),
            onClick = handleBack
        )

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            when (val uiState = state.uiState) {

                is ReceiptUiState.Loading -> {
                    LoadingComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        textId = R.string.loading
                    )
                }

                is ReceiptUiState.Error -> {
                    FailureComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        msg = uiState.message
                    )
                }

                is ReceiptUiState.Success -> {
                    val receipt = uiState.selectedReceipt
                    receipt?.let {
                        // Show receipt summary
                        ReceiptResultRow(
                            modifier = Modifier.fillMaxWidth(),
                            iconId = R.drawable.img_shop,
                            amount = "",
                            vendor = receipt.vendor.ifEmpty { "None" },
                            quantity = stringResource(R.string.count) + receipt.items.count(),
                            subCategoryTitle = "Total:",
                            subCategory = receipt.total.toString().ifEmpty { "0.0" },
                            date = receipt.date.ifEmpty { "N/A" },
                            isExpanded = receipt.receiptId in expandedReceipts,
                            onReceiptSelected = {
                                receiptViewModel.onEvent(ReceiptEvent.ToggleReceipt(receipt.receiptId))
                            },
                            onDeleteClicked = {
                                receiptViewModel.onEvent(ReceiptEvent.SetTempReceiptId(receipt.receiptId))
                                receiptViewModel.onEvent(ReceiptEvent.ShowDeleteReceiptDialog(true))
                            },
                            onButtonClick = {
                                // Parse as transaction
                            }
                        )

                        VerticalSpacer(8)

                        // List of receipt items
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = secondaryBgColor)
                                .weight(1f)
                                .padding(horizontal = 12.sdp),
                            contentPadding = PaddingValues(bottom = 12.sdp),
                            verticalArrangement = Arrangement.spacedBy(10.sdp)
                        ) {
                            items(receipt.items) { item ->
                                ReceiptResultRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    iconId = R.drawable.img_shopping,
                                    iconSize = 30,
                                    maxHeight = 50,
                                    spacer = 7,
                                    headingStyle = SubtitleLarge,
                                    amount = item.name.ifEmpty { "None" },
                                    vendor = "",
                                    quantity = "Qty: ${item.quantity}",
                                    subCategoryTitle = "Price per item",
                                    subCategory = item.pricePerItem.toString().ifEmpty { "0.0" },
                                    dateTitle = "Category:",
                                    date = item.category.ifEmpty { "None" },
                                    isExpanded = item.itemId in expandedReceiptItems,
                                    onDeleteClicked = {
                                        receiptViewModel.onEvent(ReceiptEvent.SetTempItemId(item.itemId))
                                        receiptViewModel.onEvent(ReceiptEvent.ShowDeleteItemDialog(true))
                                    },
                                    onButtonClick = {
                                        // Parse item to transaction
                                    },
                                    onReceiptSelected = {
                                        receiptViewModel.onEvent(ReceiptEvent.ToggleReceiptItem(item.itemId))
                                    }
                                )
                            }
                        }
                    }?:run {
                        EmptyComponent(
                        modifier = Modifier.fillMaxSize(),
                        image = R.drawable.img_shopping,
                        text = R.string.no_processed_receipts_available_yet
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
        NativeAd(
            context = context as Activity,
            adKey = AdKeys.ReceiptNative.name,
            placementKey = RemoteConfigKeys.RECEIPT_NATIVE_ENABLED,
            layoutKey = RemoteConfigKeys.RECEIPT_NATIVE_LAYOUT,
            screenName = "Receipt_Result_Screen_Bottom",
            commonAdsUtil = commonAdsUtil
        )
    }
}


/*
@Composable
fun ReceiptResultScreen(modifier: Modifier =Modifier, receiptViewModel: ReceiptViewModel = hiltViewModel(), navigateBack:()->Unit){
    val state by receiptViewModel.uiState.collectAsState()
    val expandedReceipts by receiptViewModel.expandedReceipts.collectAsState()
    val expandedReceiptItems by receiptViewModel.expandedReceiptItems.collectAsState()
    val handleBack = {
        navigateBack()
    }
    BackHandler { handleBack() }
    if(state.showDeleteReceiptDialog || state.showDeleteItemDialog){
        SimpleAlertDialog(
            title = if(state.showDeleteReceiptDialog) {
                stringResource(R.string.discard_receipt)
            }else{
                stringResource(R.string.discard_receipt_item)
            },
            msg = if(state.showDeleteReceiptDialog) {
                stringResource(R.string.are_you_sure_you_want_to_discard_this_receipt)
            }else{
                stringResource(R.string.are_you_sure_you_want_to_discard_this_receipt_item)
            }, positiveText = stringResource(R.string.discard), negativeText = stringResource(R.string.cancel)
        ) {
            if(it){
                if(state.showDeleteReceiptDialog) {
                    receiptViewModel.onEvent(ReceiptEvent.DeleteReceipt)
                    handleBack()
                }else{
                    receiptViewModel.onEvent(ReceiptEvent.DeleteItem)
                }
            }
            if(state.showDeleteReceiptDialog) {
                receiptViewModel.onEvent(ReceiptEvent.ShowDeleteReceiptDialog(false))
            }else{
                receiptViewModel.onEvent(ReceiptEvent.ShowDeleteItemDialog(false))
            }
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(R.string.receipt_results),
            onClick = { handleBack()}
        )
        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            state.receipt?.let { receipt ->
                    ReceiptResultRow(
                        modifier = Modifier.fillMaxWidth(),
                        iconId = R.drawable.img_shop,
                        amount = "",
                        vendor = receipt.vendor.ifEmpty { "None" },
                        quantity = "",
                        subCategoryTitle = "Total:",
                        subCategory = receipt.total.toString().ifEmpty { "0.0" },
                        date = receipt.date.ifEmpty { "N/A" },
                        isExpanded = receipt.receiptId in expandedReceipts,
                        onReceiptSelected = {
                            receiptViewModel.onEvent(ReceiptEvent.ToggleReceipt(receipt.receiptId))
                        },
                        onDeleteClicked = {
                            receiptViewModel.onEvent(ReceiptEvent.SetTempReceiptId(receiptId = receipt.receiptId))
                            receiptViewModel.onEvent(ReceiptEvent.ShowDeleteReceiptDialog(true))
                        },
                        onButtonClick = {
                            //parse receipt with all its data and add as transaction
                        })
                    VerticalSpacer(8)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = secondaryBgColor)
                            .weight(1f)
                            .padding(horizontal = 12.sdp),
                        contentPadding = PaddingValues(bottom = 12.sdp),
                        verticalArrangement = Arrangement.spacedBy(10.sdp)
                    ) {
                        items(receipt.items) { item ->
                            ReceiptResultRow(
                                modifier = Modifier.fillMaxWidth(),
                                iconId = R.drawable.img_shopping,
                                iconSize = 30,
                                maxHeight = 50,
                                spacer = 7,
                                headingStyle = SubtitleLarge,
                                amount = item.name.ifEmpty { "None" },
                                vendor = "",
                                quantity = "Qty: ${item.quantity}",
                                subCategoryTitle = "Price per item",
                                subCategory = item.pricePerItem.toString().ifEmpty { "0.0" },
                                dateTitle = "Category:",
                                date = item.category.ifEmpty { "None" },
                                isExpanded = item.itemId in expandedReceiptItems,
                                onDeleteClicked = {
                                    receiptViewModel.onEvent(ReceiptEvent.SetTempItemId(itemId = item.itemId))
                                    receiptViewModel.onEvent(ReceiptEvent.ShowDeleteItemDialog(true))
                                },
                                onButtonClick = {
                                    //parse receipt item and add as transaction
                                },
                                onReceiptSelected = {
                                    receiptViewModel.onEvent(ReceiptEvent.ToggleReceiptItem(item.itemId))
                                })
                        }
                    }
            }?:run {
                EmptyComponent(
                    modifier = Modifier.fillMaxSize(),
                    image = R.drawable.img_shopping, text = R.string.no_processed_receipts_available_yet
                )
            }
        }
    }
}*/
