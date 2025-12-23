package com.example.budgetly.ui.assistant.screens.chat

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.ads.AdKeys
import com.example.budgetly.ads.CommonAdsUtil
import com.example.budgetly.ads.NativeAd
import com.example.budgetly.data.remote.datasources.remoteConfig.RemoteConfigKeys
import com.example.budgetly.ui.assistant.AssistantViewModel
import com.example.budgetly.ui.assistant.events.AssistantEvent
import com.example.budgetly.ui.assistant.screens.chat.content.ChatMessageBubble
import com.example.budgetly.ui.assistant.state.AssistantUiState
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.dialog.SimpleAlertDialog
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
fun AssistantScreen(
    modifier: Modifier = Modifier,
    assistantViewModel: AssistantViewModel = hiltViewModel(),
    commonAdsUtil: CommonAdsUtil,
    navigateToAssistantHistory: () -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val assistantState by assistantViewModel.assistantState.collectAsState()
    val uiState  = assistantState.uiState
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val handleBack = {
        log("inside.handleBack")
        navigateBack()
    }
    BackHandler {
        handleBack()
    }
    LaunchedEffect((uiState as? AssistantUiState.Success)?.chatMessages?.size ?: 0) {
        listState.animateScrollToItem((uiState as? AssistantUiState.Success)?.chatMessages?.size ?: 0)
    }
    if(assistantState.showDeleteResponseDialog){
        SimpleAlertDialog(
            title = stringResource(R.string.delete_response),
            msg = stringResource(R.string.are_you_sure_you_want_to_delete_this_response),
            positiveText = stringResource(R.string.delete), negativeText = stringResource(R.string.cancel)
        ) {
            if(it){
                assistantViewModel.onEvent(AssistantEvent.DeleteResponse)
            }
            assistantViewModel.onEvent(AssistantEvent.ShowDeleteResponseDialog(false))
        }
    }
    if(assistantState.startNewChatDialog){
        SimpleAlertDialog(
            title = stringResource(R.string.new_chat),
            msg = stringResource(R.string.do_you_want_to_start_a_new_conversation),
            positiveText = stringResource(R.string.yes), negativeText = stringResource(R.string.no)
        ) {
            if(it){
                keyboardController?.show()
                focusManager.moveFocus(FocusDirection.Enter)
                assistantViewModel.onEvent(AssistantEvent.ResetChatScreen)
            }
            assistantViewModel.onEvent(AssistantEvent.ShowNewChatDialog(false))
        }
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(secondaryBgColor)) {
        TopBar(title = "Chat With AI Assistant", icon3 = R.drawable.icon_history, onClickIcon3 = {
            //history click
            navigateToAssistantHistory()
        }) {
            handleBack()
        }
        NativeAd(
            context = context as Activity,
            adKey = AdKeys.AIAssistantNative.name,
            placementKey = RemoteConfigKeys.ASSISTANT_NATIVE_ENABLED,
            layoutKey = RemoteConfigKeys.ASSISTANT_NATIVE_LAYOUT,
            screenName = "AI_Assistant_Screen_Top",
            commonAdsUtil = commonAdsUtil
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is AssistantUiState.Loading -> {
                    log("uiState:UiState.Loading")
                    LoadingComponent(
                        modifier = Modifier
                            .fillMaxSize(),
                        textId = R.string.loading
                    )
                }

                is AssistantUiState.Error -> {
                    log("uiState:UiState.Error")
                    FailureComponent(
                        modifier = Modifier.fillMaxSize(),
                        msg = uiState.message
                    ) {
                        assistantViewModel.onEvent(AssistantEvent.Retry)
                    }
                }

                is AssistantUiState.Success -> {
                    log("uiState:Success:$uiState chatMessages: ${uiState.chatMessages}")
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.sdp, vertical = 8.sdp),
                        contentPadding = PaddingValues(bottom = 64.sdp, top = 12.sdp),
                        verticalArrangement = Arrangement.spacedBy(10.sdp),
                        state = listState
                    ) {
                        items(uiState.chatMessages) { message ->
                            ChatMessageBubble(
                                chatMessage = message,
                                onDelete = {
                                    message.responseId?.let {
                                        assistantViewModel.onEvent(AssistantEvent.SetTempResponseId(it))
                                        assistantViewModel.onEvent(AssistantEvent.ShowDeleteResponseDialog(true))
                                    }
                                },
                                onCopy = {
                                    assistantViewModel.onEvent(AssistantEvent.CopyText(AnnotatedString(message.content)))
                                },
                                onShare = {
                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, message.content)
                                    }
                                    context.startActivity(
                                        Intent.createChooser(intent, "Share with")
                                    )
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
                                assistantViewModel.onEvent(AssistantEvent.ShowNewChatDialog(true))
                            },
                        painter = painterResource(R.drawable.icon_new_chat),
                        contentDescription = "Create New Chat"
                    )
                }

                else -> {
                    log("uiState:UiState.Idle")
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
                .fillMaxWidth()
                .background(secondaryBgColor)
                .padding(8.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextField(
                modifier = Modifier.weight(1f),
                text = assistantState.query,
                hint = "Ask me anything...",
                onValueChange = { assistantViewModel.onEvent(AssistantEvent.SetQuery(it)) },
                iconCrossClick = { assistantViewModel.onEvent(AssistantEvent.SetQuery("")) }
            )
            HorizontalSpacer(8)
            Image(
                painter = painterResource(R.drawable.icon_send),
                contentDescription = "Send",
                modifier = Modifier
                    .size(24.sdp)
                    .clickable {
                        keyboardController?.hide()
                        focusManager.clearFocus(force = true)
                        assistantViewModel.onEvent(AssistantEvent.SendQuery)
                    }
            )
        }
    }
}
