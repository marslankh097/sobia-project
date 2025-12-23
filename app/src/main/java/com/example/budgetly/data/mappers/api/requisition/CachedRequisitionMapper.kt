package com.example.budgetly.data.mappers.api.requisition
import com.example.budgetly.data.local.database.entities.api.nordigen.CachedRequisition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Requisition → CachedRequisition
fun com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition.toCachedRequisition(): CachedRequisition {
    return CachedRequisition(
        requisitionId = this.id,
        created = this.created,
        redirect = this.redirect,
        status = this.status,
        institutionId = this.institution_id,
        agreement = this.agreement,
        reference = this.reference,
        accounts = this.accounts,
        userLanguage = this.user_language,
        link = this.link,
        ssn = this.ssn,
        accountSelection = this.account_selection,
        redirectImmediate = this.redirect_immediate
    )
}

// CachedRequisition → Requisition
fun CachedRequisition.toRequisition(): com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition {
    return com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition(
        id = this.requisitionId,
        created = this.created,
        redirect = this.redirect,
        status = this.status,
        institution_id = this.institutionId,
        agreement = this.agreement,
        reference = this.reference,
        accounts = this.accounts,
        user_language = this.userLanguage,
        link = this.link,
        ssn = this.ssn,
        account_selection = this.accountSelection,
        redirect_immediate = this.redirectImmediate
    )
}
fun List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition>.toCachedRequisitionList(): List<CachedRequisition> =
    this.map { it.toCachedRequisition() }

fun List<CachedRequisition>.toRequisitionList(): List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition> =
    this.map { it.toRequisition() }
fun Flow<List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition>>.toCachedRequisitionFlow(): Flow<List<CachedRequisition>> =
    this.map { it.toCachedRequisitionList() }

fun Flow<List<CachedRequisition>>.toRequisitionFlow(): Flow<List<com.example.budgetly.data.remote.remote_models.banking.requisition.Requisition>> =
    this.map { it.toRequisitionList() }

