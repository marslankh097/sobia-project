package com.example.budgetly.ui.cash.transaction.screens.transaction_display.content

import GraphChart
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.demo.budgetly.R
import com.example.budgetly.ui.cash.transaction.state.TransactionDisplayState
import com.example.budgetly.ui.cash.transaction.state.TransactionGraphState
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.TypoH7
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import ir.kaaveh.sdpcompose.sdp

@Composable
fun TransactionsGraphPage(
    transactionTotal:String,
    graphState: TransactionGraphState,
    transactionDisplayState: TransactionDisplayState,
    onTryAgain:()->Unit,
    changeGraphType:()->Unit,

) {
    Column(
        Modifier
            .fillMaxSize().background(color = itemBgColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (graphState.isLoading) {
            LoadingComponent(
                modifier = Modifier.fillMaxSize(),
                R.string.loading_graph
            )
        } else if (graphState.error != null) {
            FailureComponent(
                modifier = Modifier.fillMaxSize(),
                msg = graphState.error
            ) {
                onTryAgain()
            }
        } else {
            if(graphState.graphPoints.isEmpty()){
                EmptyComponent(
                    modifier = Modifier.fillMaxSize().background(secondaryBgColor ),
                    image = R.drawable.img_no_transactions,
                    text = R.string.no_transaction_created_yet
                )
            }else{
                VerticalSpacer()
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.sdp), verticalAlignment = Alignment.CenterVertically){
                    Row(modifier = Modifier.fillMaxWidth().weight(1f).background(shape = RoundedCornerShape(5.sdp), color = itemBgColor)
                        .padding(6.sdp)
                        , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround){
                        Text(
                            text = "Total Transactions Amount:",
                            style = TypoH7,
                            color = primaryColor
                        )
                        HorizontalSpacer(8)
                        Text(
                            text = transactionTotal,
                            style = TypoH7,
                            color = secondaryColor
                        )
                    }
                    HorizontalSpacer(8)
                    Image(
                        modifier = Modifier.size(30.sdp).padding(3.sdp).safeClickAble { changeGraphType()},
                        painter = painterResource(
                            if (graphState.isBar) R.drawable.icon_line_chart else R.drawable.icon_bar_chart
                        ),
                        contentDescription = null
                    )
                }
                VerticalSpacer()
                GraphChart(
                    modifier = Modifier.padding(4.sdp),
                    graphPoints = graphState.graphPoints,
                    barModelProducer =  graphState.barModelProducer,
                    lineModelProducer = graphState.lineModelProducer,
                    isBar = graphState.isBar
                )
            }
        }
    }
}
