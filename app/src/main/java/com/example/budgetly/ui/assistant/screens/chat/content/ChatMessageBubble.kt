package com.example.budgetly.ui.assistant.screens.chat.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.demo.budgetly.R
import com.example.budgetly.domain.models.api.assistant.ChatMessageModel
import com.example.budgetly.utils.BodyMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ChatMessageBubble(
    chatMessage: ChatMessageModel,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    val bubbleColor = if (chatMessage.isUser) primaryColor else greyShade1
    val textColor = if (chatMessage.isUser) white else textColor
    val cornerShape = if (chatMessage.isUser) {
        RoundedCornerShape(topStart = 12.sdp, topEnd = 0.sdp, bottomEnd = 12.sdp, bottomStart = 12.sdp)
    } else {
        RoundedCornerShape(topStart = 0.sdp, topEnd = 12.sdp, bottomEnd = 12.sdp, bottomStart = 12.sdp)
    }

    val avatar = if (chatMessage.isUser) R.drawable.img_user else R.drawable.img_ai_assistant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.sdp, vertical = 4.sdp),
        horizontalArrangement = if (chatMessage.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!chatMessage.isUser) {
            Image(
                painter = painterResource(id = avatar),
                contentDescription = "AI Avatar",
                modifier = Modifier
                    .size(32.sdp)
                    .clip(CircleShape)
            )
            HorizontalSpacer(6)
        }

        Column(horizontalAlignment = if (chatMessage.isUser) Alignment.End else Alignment.Start) {
            Box(
                modifier = Modifier
                    .background(bubbleColor, cornerShape)
                    .padding(12.sdp)
                    .widthIn(max = 280.sdp)
            ) {
                Text(
                    text = chatMessage.content,
                    color = textColor,
                    style = BodyMedium,
                    textAlign = TextAlign.Start
                )
            }

            if (!chatMessage.isUser && chatMessage.responseId != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.sdp),
                    modifier = Modifier.padding(start = 8.sdp, top = 4.sdp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_copy),
                        contentDescription = "Copy",
                        colorFilter = ColorFilter.tint(secondaryColor),
                        modifier = Modifier
                            .size(16.sdp)
                            .clickable { onCopy() }
                    )
                    Image(
                        painter = painterResource(R.drawable.icon_share),
                        contentDescription = "Share",
                        colorFilter = ColorFilter.tint(secondaryColor),
                        modifier = Modifier
                            .size(16.sdp)
                            .clickable {onShare() }
                    )
                    Image(
                        painter = painterResource(R.drawable.icon_delete),
                        contentDescription = "Delete",
                        colorFilter = ColorFilter.tint(secondaryColor),
                        modifier = Modifier
                            .size(16.sdp)
                            .clickable { onDelete() }
                    )
                }
            }
        }

        if (chatMessage.isUser) {
            HorizontalSpacer(6)
            Image(
                painter = painterResource(id = avatar),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(32.sdp)
                    .clip(CircleShape)
            )
        }
    }
}
