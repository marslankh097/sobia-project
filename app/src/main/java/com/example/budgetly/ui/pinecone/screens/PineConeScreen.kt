package com.example.budgetly.ui.pinecone.screens
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ui.pinecone.PineConeChatViewModel
import com.example.budgetly.ui.pinecone.events.PineConeEvent
import com.example.budgetly.ui.pinecone.screens.content.MessageBubble
import com.example.budgetly.ui.pinecone.state.PineConeUiState
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.log
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.CustomTextField
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp


@Composable
fun PineConeScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    chatViewModel: PineConeChatViewModel = hiltViewModel()
) {
    val state by chatViewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    val handleBack = {
        log("inside.handleBack")
        navigateBack()
    }
    BackHandler {
        handleBack()
    }

    Column(modifier = modifier.fillMaxSize().background(secondaryBgColor)) {
        TopBar(title = "AI Assistant"){
            handleBack()
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(secondaryBgColor)
        ) {
            when (val uiState = state.uiState) {
                is PineConeUiState.Loading -> {
                    LoadingComponent(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textId = R.string.loading
                    )
                }

                is PineConeUiState.Error -> {
                    FailureComponent(
                        modifier = Modifier.fillMaxSize(),
                        msg = uiState.message
                    ) {
                        chatViewModel.onEvent(PineConeEvent.SendQuery)
                    }
                }

                is PineConeUiState.Success -> {
                    val listState = rememberLazyListState()
                    LaunchedEffect(uiState.messages.size) {
                        listState.animateScrollToItem(uiState.messages.size - 1)
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(secondaryBgColor)
                            .padding(horizontal = 12.sdp, vertical = 8.sdp),
                        state = listState,
                        contentPadding = PaddingValues(bottom = 30.sdp, top = 12.sdp),
                        verticalArrangement = Arrangement.spacedBy(10.sdp)
                    ) {
                        items(uiState.messages) { message ->
                            MessageBubble(chatMessage = message)
                        }
                    }
                }

                PineConeUiState.Idle -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ask me anything...", color = textColor, style = SubtitleLarge)
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(8.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                modifier = Modifier.weight(1f),
                text = state.query,
                hint = "Ask me anything...",
                onValueChange = { chatViewModel.onEvent(PineConeEvent.SetQuery(it)) },
                iconCrossClick = { chatViewModel.onEvent(PineConeEvent.SetQuery("")) }
            )
            HorizontalSpacer(8)
            Image(
                painter = painterResource(R.drawable.icon_send),
                contentDescription = "Send",
                modifier = Modifier
                    .size(20.sdp)
                    .safeClickAble {
                        keyboardController?.hide()
                        focusManager.clearFocus(force = true)
                        chatViewModel.onEvent(PineConeEvent.SendQuery)
                    }
            )
        }
    }
}





