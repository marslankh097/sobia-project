package com.example.budgetly.ui.banking.transactions.content
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
import androidx.compose.ui.Modifier
import com.demo.budgetly.R
import com.example.budgetly.utils.isIncome
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.toDateString
import com.example.budgetly.utils.toSafeEmptyString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun TransactionListContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>,
    errorMessage: String?,
    tryAgainClick:()->Unit
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 12.sdp)) {
        when {
            isLoading ->{
                LoadingComponent(
                    textId = R.string.fetching_transactions
                )
            }
            errorMessage != null ->{
                FailureComponent(
                    modifier = Modifier.fillMaxSize(),
                    msg = errorMessage
                ){
                    tryAgainClick()
                }
            }
            transactions.isEmpty() -> {
                EmptyComponent(
                    modifier = Modifier.fillMaxSize(),
                    image = R.drawable.img_no_bank_accounts, text = R.string.no_transactions_available
                )
            }
            else -> {
                LazyColumn( modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = secondaryBgColor)
                    .padding(horizontal = 12.sdp),
                    contentPadding = PaddingValues(bottom = 8.sdp),
                    verticalArrangement = Arrangement.spacedBy(10.sdp)) {
                    items(transactions) { transaction ->
                        TransactionRow(modifier = Modifier.fillMaxWidth(),
                            title = transaction.transactionAmount.amount.toSafeEmptyString() + " " +transaction.transactionAmount.currency.toSafeEmptyString(),
                            subTitle = transaction.bookingDate.let {
                              it?.toDateString(alreadyPattern = "yyyy-MM-dd" )  ?:it.toSafeEmptyString()
                            },
                            iconId = if(transaction.transactionAmount.amount.isIncome()) R.drawable.img_income else R.drawable.img_expense
                        ) {
                            // on transaction clicked
                        }
                    }
                }
            }
        }
    }

}