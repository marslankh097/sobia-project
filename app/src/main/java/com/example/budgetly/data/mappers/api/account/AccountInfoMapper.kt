package com.example.budgetly.data.mappers.api.account

import com.example.budgetly.data.remote.remote_models.banking.account.AccountInfo
import com.example.budgetly.domain.models.api.banking.account.AccountInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun AccountInfo.toAccountInfoModel(): com.example.budgetly.domain.models.api.banking.account.AccountInfoModel {
    return AccountInfoModel(
        resourceId = this.resourceId,
        iban = this.iban,
        bban = this.bban,
        scan = this.scan,
        msisdn = this.msisdn,
        currency = this.currency,
        ownerName = this.ownerName,
        name = this.name,
        displayName = this.displayName,
        product = this.product,
        cashAccountType = this.cashAccountType,
        status = this.status,
        bic = this.bic,
        linkedAccounts = this.linkedAccounts,
        maskedPan = this.maskedPan,
        usage = this.usage,
        details = this.details,
        ownerAddressUnstructured = this.ownerAddressUnstructured,
        ownerAddressStructured = this.ownerAddressStructured?.toOwnerAddressStructuredModel(),
        additionalAccountData = this.additionalAccountData?.toAdditionalAccountDataModel()
    )
}

fun com.example.budgetly.domain.models.api.banking.account.AccountInfoModel.toAccountInfo(): AccountInfo {
    return AccountInfo(
        resourceId = this.resourceId,
        iban = this.iban,
        bban = this.bban,
        scan = this.scan,
        msisdn = this.msisdn,
        currency = this.currency,
        ownerName = this.ownerName,
        name = this.name,
        displayName = this.displayName,
        product = this.product,
        cashAccountType = this.cashAccountType,
        status = this.status,
        bic = this.bic,
        linkedAccounts = this.linkedAccounts,
        maskedPan = this.maskedPan,
        usage = this.usage,
        details = this.details,
        ownerAddressUnstructured = this.ownerAddressUnstructured,
        ownerAddressStructured = this.ownerAddressStructured?.toOwnerAddressStructured(),
        additionalAccountData = this.additionalAccountData?.toAdditionalAccountData()
    )
}

fun List<AccountInfo>.toAccountInfoModelList(): List<com.example.budgetly.domain.models.api.banking.account.AccountInfoModel> =
    this.map { it.toAccountInfoModel() }

fun List<com.example.budgetly.domain.models.api.banking.account.AccountInfoModel>.toAccountInfoList(): List<AccountInfo> =
    this.map { it.toAccountInfo() }

fun Flow<List<AccountInfo>>.toAccountInfoModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountInfoModel>> =
    this.map { it.toAccountInfoModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.account.AccountInfoModel>>.toAccountInfoFlow(): Flow<List<AccountInfo>> =
    this.map { it.toAccountInfoList() }
