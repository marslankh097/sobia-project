package com.example.budgetly.data.mappers.api.assistant

import com.example.budgetly.data.local.database.entities.api.assistant.ChatEntity
import com.example.budgetly.domain.models.api.assistant.ChatModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun ChatEntity.toChatModel(count:Int): ChatModel =
    ChatModel(
        chatId = chatId,
        date = date,
        title = title,
        chatCount = count
    )

fun ChatModel.toChatEntity(): ChatEntity =
    ChatEntity(
        chatId = chatId,
        date = date,
        title = title
    )
fun List<ChatEntity>.toChatModelList(count:Int): List<ChatModel> =
    this.map { it.toChatModel(count) }

fun List<ChatModel>.toChatEntityList(): List<ChatEntity> =
    this.map { it.toChatEntity() }
fun Flow<List<ChatEntity>>.toChatModelFlow(count:Int): Flow<List<ChatModel>> =
    this.map { it.toChatModelList(count) }

fun Flow<List<ChatModel>>.toChatEntityFlow(): Flow<List<ChatEntity>> =
    this.map { it.toChatEntityList() }
