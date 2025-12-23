package com.example.budgetly.ui.pinecone.events

sealed class PineConeEvent {
    data class SetQuery(val query: String) : PineConeEvent()
    object SendQuery : PineConeEvent()
}
