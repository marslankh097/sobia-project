package com.example.budgetly.data.mappers.api.agreement

import com.example.budgetly.data.remote.remote_models.banking.agreements.PaginatedAgreementList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun PaginatedAgreementList.toPaginatedAgreementListModel(): com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel {
    return com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel(
        count = this.count,
        next = this.next,
        previous = this.previous,
        results = this.results.map { it.toEndUserAgreementModel() }
    )
}

fun com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel.toPaginatedAgreementList(): PaginatedAgreementList {
    return PaginatedAgreementList(
        count = this.count,
        next = this.next,
        previous = this.previous,
        results = this.results.map { it.toEndUserAgreement() }
    )
}

fun List<PaginatedAgreementList>.toPaginatedAgreementListModelList(): List<com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel> =
    this.map { it.toPaginatedAgreementListModel() }

fun List<com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel>.toPaginatedAgreementListList(): List<PaginatedAgreementList> =
    this.map { it.toPaginatedAgreementList() }

fun Flow<List<PaginatedAgreementList>>.toPaginatedAgreementListModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel>> =
    this.map { it.toPaginatedAgreementListModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.agreement.PaginatedAgreementListModel>>.toPaginatedAgreementListFlow(): Flow<List<PaginatedAgreementList>> =
    this.map { it.toPaginatedAgreementListList() }
