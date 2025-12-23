package com.example.budgetly.data.mappers.api.account
import com.example.budgetly.data.remote.remote_models.banking.account.AdditionalAccountData
import com.example.budgetly.domain.models.api.banking.account.AdditionalAccountDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AdditionalAccountData.toAdditionalAccountDataModel(): com.example.budgetly.domain.models.api.banking.account.AdditionalAccountDataModel {
    return AdditionalAccountDataModel(
        secondaryIdentification = this.secondaryIdentification
    )
}

fun com.example.budgetly.domain.models.api.banking.account.AdditionalAccountDataModel.toAdditionalAccountData(): AdditionalAccountData {
    return AdditionalAccountData(
        secondaryIdentification = this.secondaryIdentification
    )
}

fun List<AdditionalAccountData>.toAdditionalAccountDataModelList(): List<com.example.budgetly.domain.models.api.banking.account.AdditionalAccountDataModel> =
    this.map { it.toAdditionalAccountDataModel() }

fun List<com.example.budgetly.domain.models.api.banking.account.AdditionalAccountDataModel>.toAdditionalAccountDataList(): List<AdditionalAccountData> =
    this.map { it.toAdditionalAccountData() }

fun Flow<List<AdditionalAccountData>>.toAdditionalAccountDataModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.account.AdditionalAccountDataModel>> =
    this.map { it.toAdditionalAccountDataModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.account.AdditionalAccountDataModel>>.toAdditionalAccountDataFlow(): Flow<List<AdditionalAccountData>> =
    this.map { it.toAdditionalAccountDataList() }
