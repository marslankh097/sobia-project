package com.example.budgetly.domain.repositories.api.pinecone


import com.example.budgetly.domain.models.api.pinecone.PineConeMessageModel

interface PineConeRepository {
    suspend fun sendMessages(messages: List<PineConeMessageModel>): Result<PineConeMessageModel>
}
