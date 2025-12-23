package com.example.budgetly.ui.banking.institution.events

sealed class InstitutionEvents {
    data object LoadInstitutions: InstitutionEvents()
    data class SelectInstitution(val institutionId:String): InstitutionEvents()
}