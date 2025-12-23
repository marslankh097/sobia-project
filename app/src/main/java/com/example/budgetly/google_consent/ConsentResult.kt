package com.example.budgetly.google_consent

sealed class ConsentResult {
    data object Granted : ConsentResult()        // user allowed personalized ads
    data object Rejected : ConsentResult()       // user denied
    data object NotRequired : ConsentResult()    // no dialog was shown (GDPR not required, or TC string already valid)
    data object Timeout : ConsentResult()        // dialog never appeared for 8 sec
    data class Error(val message: String) : ConsentResult()
}
