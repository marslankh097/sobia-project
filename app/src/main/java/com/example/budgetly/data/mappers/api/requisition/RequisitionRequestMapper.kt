package com.example.budgetly.data.mappers.api.requisition

import com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest
import com.example.budgetly.domain.models.api.banking.requisition.RequisitionRequestModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest.toRequisitionRequestModel(): com.example.budgetly.domain.models.api.banking.requisition.RequisitionRequestModel {
    return RequisitionRequestModel(
        redirect = this.redirect,
        institution_id = this.institution_id,
        agreement = this.agreement,
        reference = this.reference,
        user_language = this.user_language,
        ssn = this.ssn,
        account_selection = this.account_selection,
        redirect_immediate = this.redirect_immediate
    )
}

fun com.example.budgetly.domain.models.api.banking.requisition.RequisitionRequestModel.toRequisitionRequest(): com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest {
    return com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest(
        redirect = this.redirect,
        institution_id = this.institution_id,
        agreement = this.agreement,
        reference = this.reference,
        user_language = this.user_language,
        ssn = this.ssn,
        account_selection = this.account_selection,
        redirect_immediate = this.redirect_immediate
    )
}

fun List<com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest>.toRequisitionRequestModelList(): List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionRequestModel> =
    this.map { it.toRequisitionRequestModel() }

fun List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionRequestModel>.toRequisitionRequestList(): List<com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest> =
    this.map { it.toRequisitionRequest() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest>>.toRequisitionRequestModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionRequestModel>> =
    this.map { it.toRequisitionRequestModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionRequestModel>>.toRequisitionRequestFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.requisition.RequisitionRequest>> =
    this.map { it.toRequisitionRequestList() }


