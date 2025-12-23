package com.example.budgetly.data.mappers.api.account
import com.example.budgetly.data.remote.remote_models.banking.account.AccountDetail
import com.example.budgetly.domain.models.api.banking.account.AccountDetailModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AccountDetail.toAccountDetailModel(): AccountDetailModel {
    return AccountDetailModel(
        account = this.account.toAccountInfoModel()
    )
}

fun AccountDetailModel.toAccountDetail(): AccountDetail {
    return AccountDetail(
        account = this.account.toAccountInfo()
    )
}

fun List<AccountDetail>.toAccountDetailModelList(): List<AccountDetailModel> =
    this.map { it.toAccountDetailModel() }

fun List<AccountDetailModel>.toAccountDetailList(): List<AccountDetail> =
    this.map { it.toAccountDetail() }

fun Flow<List<AccountDetail>>.toAccountDetailModelFlow(): Flow<List<AccountDetailModel>> =
    this.map { it.toAccountDetailModelList() }

fun Flow<List<AccountDetailModel>>.toAccountDetailFlow(): Flow<List<AccountDetail>> =
    this.map { it.toAccountDetailList() }
