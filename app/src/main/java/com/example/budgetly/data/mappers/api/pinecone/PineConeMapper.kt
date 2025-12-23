package com.example.budgetly.data.mappers.api.pinecone
import com.example.budgetly.data.remote.remote_models.pinecone.ChatMessage
import com.example.budgetly.domain.models.api.pinecone.PineConeMessageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//Message (remote) ➡ ChatMessage (domain)
fun ChatMessage.toChatMessageModel(): PineConeMessageModel =  PineConeMessageModel(
    isUser = role.equals("user", ignoreCase = true),
    content = content
)



//ChatMessage (domain) ➡ Message (remote)
fun PineConeMessageModel.toChatMessage(): ChatMessage = ChatMessage(
    role = if (isUser) "user" else "assistant",
    content = content
)

fun List<ChatMessage>.toChatMessageModelList(): List<PineConeMessageModel> =
    this.map { it.toChatMessageModel() }

fun List<PineConeMessageModel>.toChatMessageList(): List<ChatMessage> =
    this.map { it.toChatMessage() }

fun Flow<List<ChatMessage>>.toChatMessageModelFlow(): Flow<List<PineConeMessageModel>> =
    this.map { it.toChatMessageModelList() }

fun Flow<List<PineConeMessageModel>>.toChatMessageFlow(): Flow<List<ChatMessage>> =
    this.map { it.toChatMessageList() }