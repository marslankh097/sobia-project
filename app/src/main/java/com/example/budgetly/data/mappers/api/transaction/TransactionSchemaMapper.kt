package com.example.budgetly.data.mappers.api.transaction
import com.example.budgetly.data.mappers.api.account.toAccountSchema
import com.example.budgetly.data.mappers.api.account.toAccountSchemaModel
import com.example.budgetly.data.mappers.api.account.toCurrencyExchangeSchema
import com.example.budgetly.data.mappers.api.account.toCurrencyExchangeSchemaModel
import com.example.budgetly.data.mappers.api.balance.toBalanceAfterTransactionSchema
import com.example.budgetly.data.mappers.api.balance.toBalanceAfterTransactionSchemaModel
import com.example.budgetly.data.remote.remote_models.banking.account.TransactionSchema
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun TransactionSchema.toTransactionSchemaModel(): com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel {
    return com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel(
        transactionId = transactionId,
        entryReference = entryReference,
        endToEndId = endToEndId,
        mandateId = mandateId,
        checkId = checkId,
        creditorId = creditorId,
        bookingDate = bookingDate,
        valueDate = valueDate,
        bookingDateTime = bookingDateTime,
        valueDateTime = valueDateTime,
        transactionAmount = transactionAmount.toTransactionAmountSchemaModel(),
        currencyExchange = currencyExchange?.toCurrencyExchangeSchemaModel(),
        creditorName = creditorName,
        creditorAccount = creditorAccount?.toAccountSchemaModel(),
        ultimateCreditor = ultimateCreditor,
        debtorName = debtorName,
        debtorAccount = debtorAccount?.toAccountSchemaModel(),
        ultimateDebtor = ultimateDebtor,
        remittanceInformationUnstructured = remittanceInformationUnstructured,
        remittanceInformationUnstructuredArray = remittanceInformationUnstructuredArray,
        remittanceInformationStructured = remittanceInformationStructured,
        remittanceInformationStructuredArray = remittanceInformationStructuredArray,
        additionalInformation = additionalInformation,
        purposeCode = purposeCode,
        bankTransactionCode = bankTransactionCode,
        proprietaryBankTransactionCode = proprietaryBankTransactionCode,
        internalTransactionId = internalTransactionId,
        balanceAfterTransaction = balanceAfterTransaction?.toBalanceAfterTransactionSchemaModel()
    )
}

fun com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel.toTransactionSchema(): TransactionSchema {
    return TransactionSchema(
        transactionId = transactionId,
        entryReference = entryReference,
        endToEndId = endToEndId,
        mandateId = mandateId,
        checkId = checkId,
        creditorId = creditorId,
        bookingDate = bookingDate,
        valueDate = valueDate,
        bookingDateTime = bookingDateTime,
        valueDateTime = valueDateTime,
        transactionAmount = transactionAmount.toTransactionAmountSchema(),
        currencyExchange = currencyExchange?.toCurrencyExchangeSchema(),
        creditorName = creditorName,
        creditorAccount = creditorAccount?.toAccountSchema(),
        ultimateCreditor = ultimateCreditor,
        debtorName = debtorName,
        debtorAccount = debtorAccount?.toAccountSchema(),
        ultimateDebtor = ultimateDebtor,
        remittanceInformationUnstructured = remittanceInformationUnstructured,
        remittanceInformationUnstructuredArray = remittanceInformationUnstructuredArray,
        remittanceInformationStructured = remittanceInformationStructured,
        remittanceInformationStructuredArray = remittanceInformationStructuredArray,
        additionalInformation = additionalInformation,
        purposeCode = purposeCode,
        bankTransactionCode = bankTransactionCode,
        proprietaryBankTransactionCode = proprietaryBankTransactionCode,
        internalTransactionId = internalTransactionId,
        balanceAfterTransaction = balanceAfterTransaction?.toBalanceAfterTransactionSchema()
    )
}

fun List<TransactionSchema>.toTransactionSchemaModelList(): List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel> =
    this.map { it.toTransactionSchemaModel() }

fun List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>.toTransactionSchemaList(): List<TransactionSchema> =
    this.map { it.toTransactionSchema() }

fun Flow<List<TransactionSchema>>.toTransactionSchemaModelFlow(): Flow<List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>> =
    this.map { it.toTransactionSchemaModelList() }

fun Flow<List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>>.toTransactionSchemaFlow(): Flow<List<TransactionSchema>> =
    this.map { it.toTransactionSchemaList() }
