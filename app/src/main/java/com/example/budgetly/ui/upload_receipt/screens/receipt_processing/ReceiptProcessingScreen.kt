package com.example.budgetly.ui.upload_receipt.screens.receipt_processing

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ui.upload_receipt.ReceiptViewModel
import com.example.budgetly.ui.upload_receipt.events.ReceiptEvent
import com.example.budgetly.ui.upload_receipt.state.ReceiptUiState
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.CoilImage
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ReceiptProcessingScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToReceiptResult: () -> Unit,
    receiptViewModel: ReceiptViewModel = hiltViewModel()
) {
    val receiptState by receiptViewModel.receiptState.collectAsState()
    val context = LocalContext.current

    BackHandler { navigateBack() }

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(R.string.upload_receipt),
            onClick = navigateBack
        )

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            // ─────── Uploaded Image ───────
            CoilImage(
                model = receiptState.uploadedFile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.sdp),
                corners = 10,
                placeholder = R.drawable.img_gallery
            )

            VerticalSpacer(12)

            // ─────── Process Button ───────
            PrimaryButtonRounded(
                gradientColors = if (receiptState.uiState is ReceiptUiState.Loading)
                    listOf(greyShade1, greyShade1)
                else listOf(primaryColor, primaryColor),
                onButtonClick = {
                    receiptViewModel.onEvent(
                        if (receiptState.uiState is ReceiptUiState.Error) ReceiptEvent.Retry(context)
                        else ReceiptEvent.Process
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (receiptState.uiState) {
                        is ReceiptUiState.Loading -> stringResource(R.string.processing)
                        is ReceiptUiState.Error -> stringResource(R.string.try_again)
                        else -> stringResource(R.string.process_receipt)
                    },
                    style = SubtitleLarge,
                    color = if (receiptState.uiState is ReceiptUiState.Loading) hintColor else white
                )
            }

            VerticalSpacer(16)

            // ─────── Handle UI State (Loading / Error / Success / Idle) ───────
            when (val uiState = receiptState.uiState) {
                is ReceiptUiState.Loading -> {
                    LoadingComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        textId = R.string.processing
                    )
                }

                is ReceiptUiState.Error -> {
                    FailureComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        showBtn = false,
                        msg = uiState.message
                    )
                }

                is ReceiptUiState.Success -> {
                    if (receiptState.shouldNavigateToResultOnce) {
                        LaunchedEffect(Unit) {
                            navigateToReceiptResult()
                            receiptViewModel.onEvent(ReceiptEvent.MarkNavigationHandled)
                        }
                    }

                }
                ReceiptUiState.Idle -> {
                    EmptyComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        image = R.drawable.img_shopping,
                        text = R.string.no_receipt_data_extracted
                    )
                }
            }
        }
    }
}

/*@Composable
fun ReceiptProcessingScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToReceiptResult:()->Unit,
    receiptViewModel: ReceiptViewModel = hiltViewModel()
) {
    val receiptState by receiptViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(R.string.upload_receipt),
            onClick = {
                handleBack()
            }
        )

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            // ─────── Uploaded Image ───────
            CoilImage(
                model = receiptState.uploadedFile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.sdp),
                corners = 10,
                placeholder = R.drawable.img_gallery
            )

            VerticalSpacer(12)

            // ─────── Dynamic Button Text ───────
            val buttonText = when {
                receiptState.isProcessing -> stringResource(R.string.processing)
                receiptState.error != null -> stringResource(R.string.try_again)
                else -> stringResource(R.string.process_receipt)
            }

            // ─────── Error Message ───────
            if (receiptState.error != null && !receiptState.isProcessing) {
                Text(
                    text = "Error: ${receiptState.error}",
                    color = red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.sdp)
                )
            }
            VerticalSpacer(12)
            // ─────── Process / Retry Button ───────
            PrimaryButtonRounded(
                gradientColors = if (receiptState.isProcessing.not())
                    listOf(primaryColor, primaryColor)
                else listOf(greyShade1, greyShade1),
                onButtonClick = {
                    receiptViewModel.onEvent(
                        if (receiptState.error != null) ReceiptEvent.Retry(context)
                        else ReceiptEvent.Process
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buttonText,
                    style = SubtitleLarge,
                    color = if (receiptState.isProcessing.not()) white else hintColor
                )
            }

            VerticalSpacer(16)

            // ─────── Loading ───────
            if (receiptState.isProcessing) {
                LoadingComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    textId = R.string.processing
                )
            }

            // ─────── Display Receipt Details ───────
            if (receiptState.error == null && !receiptState.isProcessing ) {
                receiptState.receipt?.let {
                    navigateToReceiptResult.invoke()
                }?:run {
                    EmptyComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), image = R.drawable.img_shopping,
                           text = R.string.no_receipt_data_extracted
                    )
                }
            }
        }
    }
}*/
