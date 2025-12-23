package com.example.budgetly.data.remote.remote_models.banking.error

enum class RequisitionStatus(val title: String) {
    CR("Created"),
    ID("Identified"),
    LN("Linking"),
    RJ("Rejected"),
    ER("Error"),
    SU("Succeeded"),
    EX("Expired"),
    GC("Giving Consent"),
    UA("Undergoing Authentication"),
    GA("Giving Authorization"),
    SA("Selecting Accounts");
    companion object {
        fun fromCode(code: String): RequisitionStatus? {
            return entries.find { it.name == code }
        }
    }
}
