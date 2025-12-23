package com.example.budgetly.data.mappers.api.requisition

import com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition
import com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition.toRequisitionModel(): com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel {
    return RequisitionModel(
        id = this.id,
        created = this.created,
        redirect = this.redirect,
        status = this.status,
        institution_id = this.institution_id,
        agreement = this.agreement,
        reference = this.reference,
        accounts = this.accounts,
        user_language = this.user_language,
        link = this.link,
        ssn = this.ssn,
        account_selection = this.account_selection,
        redirect_immediate = this.redirect_immediate
    )
}

fun com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel.toRequisition(): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition {
    return com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition(
        id = this.id,
        created = this.created,
        redirect = this.redirect,
        status = this.status,
        institution_id = this.institution_id,
        agreement = this.agreement,
        reference = this.reference,
        accounts = this.accounts,
        user_language = this.user_language,
        link = this.link,
        ssn = this.ssn,
        account_selection = this.account_selection,
        redirect_immediate = this.redirect_immediate
    )
}

fun List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition>.toRequisitionModelList(): List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel> =
    this.map { it.toRequisitionModel() }

fun List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel>.toRequisitionList(): List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition> =
    this.map { it.toRequisition() }

fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition>>.toRequisitionModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel>> =
    this.map { it.toRequisitionModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.requisition.RequisitionModel>>.toRequisitionFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition>> =
    this.map { it.toRequisitionList() }

