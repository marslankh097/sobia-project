package com.example.budgetly.ui.banking.requisition.events

sealed class RequisitionEvent {
    data class CreateRequisition(val institutionId: String, val redirectUrl: String) : RequisitionEvent()
    data class FetchAccounts(val requisitionId: String) : RequisitionEvent()
    object Reset : RequisitionEvent()
}