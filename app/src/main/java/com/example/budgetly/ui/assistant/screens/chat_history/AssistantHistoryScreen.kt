package com.example.budgetly.ui.assistant.screens.chat_history

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
import com.example.budgetly.ui.assistant.AssistantViewModel
import com.example.budgetly.ui.assistant.events.AssistantEvent
import com.example.budgetly.ui.assistant.screens.chat_history.content.AssistantHistoryRow
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.log
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.EmptyComponent
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toDateString
import ir.kaaveh.sdpcompose.sdp

@Composable
fun AssistantHistoryScreen(
    modifier: Modifier = Modifier,
    assistantViewModel: AssistantViewModel = hiltViewModel(),
    commonAdsUtil: CommonAdsUtil,
    navigateToAssistantScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val assistantState by assistantViewModel.assistantState.collectAsState()
    val handleBack = {
        log("inside.handleBack")
        navigateBack()
    }
    BackHandler {
        handleBack()
    }
    if(assistantState.showDeleteChatDialog || assistantState.showDeleteAllDialog){
        SimpleAlertDialog(
            title = if(assistantState.showDeleteChatDialog) {
                stringResource(R.string.delete_chat)
            }else{
                stringResource(R.string.delete_all_chats)
            },
            msg = if(assistantState.showDeleteChatDialog) {
                stringResource(R.string.are_you_sure_you_want_to_delete_this_chat)
            }else{
                stringResource(R.string.are_you_sure_you_want_to_delete_all_chats_from_your_history)
            }, positiveText = stringResource(R.string.delete), negativeText = stringResource(R.string.cancel)
        ) {
            if(it){
                if(assistantState.showDeleteChatDialog) {
                    assistantViewModel.onEvent(AssistantEvent.DeleteChat)
                }else{
                    assistantViewModel.onEvent(AssistantEvent.DeleteAllChats)
                }
            }
            if(assistantState.showDeleteChatDialog) {
                assistantViewModel.onEvent(AssistantEvent.ShowDeleteChatDialog(false))
            }else{
                assistantViewModel.onEvent(AssistantEvent.ShowDeleteAllDialog(false))
            }
        }
    }
    Column(modifier = modifier
        .fillMaxSize()
        .background(secondaryBgColor)) {
        TopBar(title = "Assistant Chat History", icon3 = if(assistantState.chats.isNotEmpty() )R.drawable.img_delete else null, onClickIcon3 =
            if(assistantState.chats.isNotEmpty()){ {
                    //delete all chats click
                    assistantViewModel.onEvent(AssistantEvent.ShowDeleteAllDialog(true))}
            }
            else null
        ) {
            handleBack()
        }
        if (assistantState.chats.isEmpty()) {
            EmptyComponent(
                modifier = Modifier.fillMaxSize(),
                image = R.drawable.icon_chat, text = R.string.no_chat_history_available
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
                items(assistantState.chats) { chat ->
                    AssistantHistoryRow(
                        modifier = Modifier.fillMaxWidth(),
                        iconId = R.drawable.icon_chat,
                        title = chat.title,
                        chatCount = chat.chatCount.toString(),
                        subTitle = chat.date.toDateString("dd_MMM_yyyy"),
                        onDeleteClicked = {
                            assistantViewModel.onEvent(AssistantEvent.SetTempChatId(chat.chatId))
                            assistantViewModel.onEvent(AssistantEvent.ShowDeleteChatDialog(true))
                        }, onTransactionSelected = {
                            assistantViewModel.onEvent(AssistantEvent.SelectChat(chat.chatId))
                            handleBack()
                        }
                    )
                    }
            }
            NativeAd(
                context = context as Activity,
                adKey = AdKeys.AIAssistantNative.name,
                placementKey = RemoteConfigKeys.ASSISTANT_HISTORY_NATIVE_ENABLED,
                layoutKey = RemoteConfigKeys.ASSISTANT_HISTORY_NATIVE_LAYOUT,
                screenName = "AI_Assistant_History_Bottom",
                commonAdsUtil = commonAdsUtil
            )
        }
    }
}