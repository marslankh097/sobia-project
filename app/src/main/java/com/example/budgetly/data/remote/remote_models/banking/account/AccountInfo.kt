package com.example.budgetly.data.remote.remote_models.banking.account

import com.example.budgetly.data.remote.remote_models.banking.account.AdditionalAccountData
import com.example.budgetly.data.remote.remote_models.banking.account.OwnerAddressStructured

data class AccountInfo(
    val resourceId: String,
    val iban: String,
    val bban: String?=null,
    val scan: String? = null,
    val msisdn: String? = null,
    val currency: String,
    val ownerName: String,
    val name: String,
    val displayName: String? = null,
    val product: String? = null,
    val cashAccountType: String? = null,
    val status: String,
    val bic: String? = null,
    val linkedAccounts: String? = null,
    val maskedPan: List<String>? = null,
    val usage: String? = null,
    val details: String? = null,
    val ownerAddressUnstructured: List<String>? = null,
    val ownerAddressStructured: OwnerAddressStructured? = null,
    val additionalAccountData: AdditionalAccountData? = null
)

//data class DetailSchema(
//    val resourceId: String? = null,
//    val iban: String? = null,
//    val bban: String? = null,
//    val scan: String? = null,
//    val msisdn: String? = null,
//    val currency: String? = null,
//    val ownerName: String? = null,
//    val name: String? = null,
//    val displayName: String? = null,
//    val product: String? = null,
//    val cashAccountType: String? = null,
//    val status: String? = null,
//    val bic: String? = null,
//    val linkedAccounts: String? = null,
//    val maskedPan: String? = null,
//    val usage: String? = null,
//    val details: String? = null,
//    val ownerAddressUnstructured: String? = null,
//    val ownerAddressStructured: OwnerAddressStructuredSchema? = null,
//    val additionalAccountData: AdditionalAccountDataSchema? = null
//)
