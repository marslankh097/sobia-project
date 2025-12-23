package com.example.budgetly.ui.pinecone.screens.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.demo.budgetly.R
import com.example.budgetly.domain.models.api.pinecone.PineConeMessageModel
import com.example.budgetly.utils.BodyMedium
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.greyShade1
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.textColor
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp
@Composable
fun MessageBubble(
    chatMessage: PineConeMessageModel,
    modifier: Modifier = Modifier
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

/*
@Composable
fun MessageBubble(
    chatMessage: ChatMessageModel,
    modifier: Modifier = Modifier
) {
    val bubbleColor = if (chatMessage.isUser) primaryColor else greyShade1
    val textColor = if (chatMessage.isUser) white else textColor
    val cornerShape = if (chatMessage.isUser) {
        RoundedCornerShape(topStart = 12.sdp, topEnd = 0.sdp, bottomEnd = 12.sdp, bottomStart = 12.sdp)
    } else {
        RoundedCornerShape(topStart = 0.sdp, topEnd = 12.sdp, bottomEnd = 12.sdp, bottomStart = 12.sdp)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.sdp, vertical = 4.sdp),
        horizontalArrangement = if (chatMessage.isUser) Arrangement.End else Arrangement.Start
    ) {
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
    }
}*/
