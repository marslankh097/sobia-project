package com.example.budgetly.data.mappers.api.requisition

import com.example.budgetly.data.remote.remote_models.banking.requisition.PaginatedRequisitionList
import com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun PaginatedRequisitionList.toPaginatedRequisitionListModel(): com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel {
    return PaginatedRequisitionListModel(
        count = this.count,
        next = this.next,
        previous = this.previous,
        results = this.results.toRequisitionModelList()
    )
}

fun com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel.toPaginatedRequisitionList(): PaginatedRequisitionList {
    return PaginatedRequisitionList(
        count = this.count,
        next = this.next,
        previous = this.previous,
        results = this.results.toRequisitionList()
    )
}

fun List<PaginatedRequisitionList>.toPaginatedRequisitionListModelList(): List<com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel> =
    this.map { it.toPaginatedRequisitionListModel() }

fun List<com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel>.toPaginatedRequisitionListList(): List<PaginatedRequisitionList> =
    this.map { it.toPaginatedRequisitionList() }

fun Flow<List<PaginatedRequisitionList>>.toPaginatedRequisitionListModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel>> =
    this.map { it.toPaginatedRequisitionListModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.requisition.PaginatedRequisitionListModel>>.toPaginatedRequisitionListFlow(): Flow<List<PaginatedRequisitionList>> =
    this.map { it.toPaginatedRequisitionListList() }

