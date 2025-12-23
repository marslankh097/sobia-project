package com.example.budgetly.ui.cash.transaction.screens.transaction_display.content
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.common.Options
import com.example.budgetly.domain.models.enums.transaction.TransactionType
import com.example.budgetly.ui.cash.transaction.events.TransactionEvent
import com.example.budgetly.ui.cash.transaction.events.TransactionInsertEvent
import com.example.budgetly.ui.cash.transaction.state.TransactionDisplayState
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.dialog.UpdateDeleteDialog
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.toDateString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun TransactionsListPage(
    transactionState: TransactionDisplayState,
    isExpense: Boolean,
    onTransactionViewModelEvent: (TransactionEvent) -> Unit,
    onTransactionViewModelInsertEvent: (TransactionInsertEvent) -> Unit,
    navigateToTransactionInsertUpdate: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // ✅ Delete confirmation dialog
        if (transactionState.showDeleteConfirmationDialog) {
            SimpleAlertDialog(
                modifier = Modifier.fillMaxWidth(),
                title = if (isExpense) "Delete Expense!" else "Delete Income",
                msg = if (isExpense) {
                    "Are you sure you want to delete this expense transaction?"
                } else {
                    "Are you sure you want to delete this income transaction?"
                },
                positiveText = stringResource(R.string.delete),
                negativeText = stringResource(R.string.cancel),
                onButtonClick = { confirmed ->
                    onTransactionViewModelEvent(
                        TransactionEvent.ShowDeleteConfirmationDialog(
                            show = false
                        )
                    )
                    if (confirmed) {
                        onTransactionViewModelEvent(TransactionEvent.DeleteTransactionById)
                    }
                }
            )
        }

        // ✅ Update/Delete options dialog
        if (transactionState.showUpdateDeleteDialog) {
            UpdateDeleteDialog(
                title = if (isExpense) stringResource(R.string.expense) else stringResource(R.string.income),
                onOptionSelected = { option ->
                    onTransactionViewModelEvent(
                        TransactionEvent.ShowDeleteUpdateDialog(
                            show = false
                        )
                    )
                    if (option == Options.Delete.name) {
                        onTransactionViewModelEvent(
                            TransactionEvent.ShowDeleteConfirmationDialog(
                                show = true
                            )
                        )
                    } else if (option.isNotEmpty()) {
                        onTransactionViewModelInsertEvent(TransactionInsertEvent.InsertUpdateMode(true))
                        navigateToTransactionInsertUpdate()
                    }
                }
            )
        }

        // ✅ Transactions list or empty view
        if (transactionState.transactions.isEmpty()) {
            EmptyComponent(
                modifier = Modifier.fillMaxSize(),
                image = R.drawable.img_no_transactions,
                text = R.string.no_transaction_created_yet
            )
        } else {
            Column(modifier = Modifier.fillMaxWidth()){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(secondaryBgColor).weight(1f)
                        .padding(horizontal = 12.sdp, vertical = 12.sdp),
                    contentPadding = PaddingValues( bottom = 60.sdp),
                    verticalArrangement = Arrangement.spacedBy(10.sdp)
                ) {
                    items(transactionState.transactions) { transaction ->
                        TransactionRow(
                            modifier = Modifier.fillMaxWidth(),
                            iconId = if (transaction.type == TransactionType.Income.name)
                                R.drawable.img_income else R.drawable.img_expense,
                            amount = transaction.amount,
                            currency = transaction.currency,
                            frequency = transaction.frequency,
                            subCategory = transaction.subCategoryName.ifEmpty { "None" },
                            date = transaction.date.toDateString("dd MMM yyyy"),
                            onTransactionSelected = { /* optional click action */ },
                            onLongPressed = {
                                onTransactionViewModelInsertEvent(TransactionInsertEvent.SetCategoryById(transaction.categoryId))
                                onTransactionViewModelInsertEvent(TransactionInsertEvent.SetSubCategoryById(transaction.subcategoryId))
                                onTransactionViewModelInsertEvent(TransactionInsertEvent.SetAccountById(transaction.accountId))
                                onTransactionViewModelInsertEvent(TransactionInsertEvent.PrepareInsertUpdate(transactionId = transaction.transactionId, amount = transaction.amount, currency = transaction.currency, notes = transaction.description.orEmpty(), frequency = transaction.frequency, dateTime = transaction.date))
                                onTransactionViewModelEvent(TransactionEvent.ShowDeleteUpdateDialog(show = true))
                            }
                        )
                    }
                }
            }
        }

        // ✅ Floating action button to add new transaction
        Image(
            modifier = Modifier
                .size(70.sdp)
                .align(Alignment.BottomEnd)
                .padding(end = 30.sdp, bottom = 30.sdp)
                .clickable {
                    onTransactionViewModelInsertEvent(TransactionInsertEvent.InsertUpdateMode(false))
                    onTransactionViewModelInsertEvent(TransactionInsertEvent.PrepareInsertUpdate())
                    onTransactionViewModelInsertEvent(
                        TransactionInsertEvent.SetCategorySubCategoryAccount(transactionState.categoryModel, transactionState.subCategoryModel, transactionState.accountModel))
                    navigateToTransactionInsertUpdate()
                },
            painter = painterResource(R.drawable.icon_add_gradient),
            contentDescription = "Add New Transaction"
        )
    }
}

