package com.example.budgetly.ui.banking.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.domain.usecases.api_usecases.banking.AllTransactionUseCases
import com.example.budgetly.domain.usecases.api_usecases.banking.ExpenseUseCases
import com.example.budgetly.domain.usecases.api_usecases.banking.IncomeUseCases
import com.example.budgetly.ui.banking.transactions.events.TransactionEvent
import com.example.budgetly.ui.banking.transactions.state.TransactionState
import com.example.budgetly.utils.isIncome
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- TransactionViewModel.kt ---
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val allTransactionUseCases: AllTransactionUseCases,
    private val expenseUseCases: ExpenseUseCases,
    private val incomeUseCases: IncomeUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionState())
    var uiState: StateFlow<TransactionState> = _uiState

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.ChangeTab -> {
                _uiState.update {
                    it.copy(
                        selectedTabIndex = event.index
                    )
                }
            }

            is TransactionEvent.LoadAllTransactions -> {
                loadTransactions(
                    loadFunc = {
                        allTransactionUseCases.getPastDaysTransactions(
                            event.accountId,7
                        )
                    },
                    onResult = { transactions->
                        _uiState.update {
                            it.copy(
                                allTransactions = transactions,
                                incomeTransactions = transactions.filter { transaction->
                                    transaction.transactionAmount.amount.isIncome()
                                },
                                expenseTransactions = transactions.filter { transaction->
                                    transaction.transactionAmount.amount.isIncome().not()
                                }
                            )
                        }

                    }
                )
            }

//            is TransactionEvent.LoadIncomeTransactions -> {
//                loadTransactions(
//                    loadFunc = {
//                        incomeUseCases.getPastDaysIncome(
//                            event.accountId,7)
//                    },
//                    onResult = { transactions->
//                        _uiState.update {
//                            it.copy(
//                                incomeTransactions = transactions
//                            )
//                        }
//                    }
//                )
//            }
//
//            is TransactionEvent.LoadExpenseTransactions -> {
//                loadTransactions(
//                    loadFunc = {
//                        expenseUseCases.getPastDaysExpenses(
//                            event.accountId, 7
//                        )
//                    },
//                    onResult = { transactions->
//                        _uiState.update {
//                            it.copy(
//                                expenseTransactions = transactions
//                            )
//                        }
//                    }
//                )
//            }
        }
    }

    private fun loadTransactions(
        loadFunc: suspend () -> List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>,
        onResult: (List<com.example.budgetly.domain.models.api.banking.transaction.TransactionSchemaModel>) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true, errorMessage = null
                )
            }
            try {
                val transactions = loadFunc()
                onResult(transactions)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Unknown error"
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }
}