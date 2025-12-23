package com.example.budgetly.domain.usecases.api_usecases.pinecone

import com.example.budgetly.domain.models.api.pinecone.PineConeMessageModel
import com.example.budgetly.domain.repositories.api.pinecone.PineConeRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
data class SendQueryUseCase @Inject constructor(
    val sendMessages: SendMessages
)
@Singleton
class SendMessages @Inject constructor(
    private val repository: PineConeRepository
) {
    suspend operator fun invoke(history: List<PineConeMessageModel>): Result<PineConeMessageModel>{
        return repository.sendMessages(history)
    }
}