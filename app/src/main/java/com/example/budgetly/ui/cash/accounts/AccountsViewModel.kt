package com.example.budgetly.ui.cash.accounts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.data.local.datasources.data_store.PreferenceKeys
import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.enums.account.AccountCategory
import com.example.budgetly.domain.models.enums.account.AccountType
import com.example.budgetly.domain.usecases.db_usecases.cash.AccountUseCases
import com.example.budgetly.domain.usecases.system_usecases.PreferenceUseCases
import com.example.budgetly.ui.cash.accounts.events.AccountEvent
import com.example.budgetly.ui.cash.accounts.state.AccountDisplayState
import com.example.budgetly.ui.cash.accounts.state.AccountInsertState
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountUseCases: AccountUseCases,
    private val preferenceUseCases: PreferenceUseCases
) : ViewModel() {

    private val _insertState = MutableStateFlow(AccountInsertState())
    val insertState: StateFlow<AccountInsertState> = _insertState.asStateFlow()

    private val _accountState = MutableStateFlow(AccountDisplayState())
    val accountState: StateFlow<AccountDisplayState> = _accountState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

//    private val _tempAccount = MutableStateFlow<AccountModel?>(null)
//    val tempAccount: StateFlow<AccountModel?> = _tempAccount.asStateFlow()
//
//    private val _selectedAccount = MutableStateFlow<AccountModel?>(null)
//    val selectedAccount: StateFlow<AccountModel?> = _selectedAccount.asStateFlow()

//    private val _tempCurrency = MutableStateFlow("PKR")
//    val tempCurrency: StateFlow<String> = _tempCurrency.asStateFlow()

    init {
        viewModelScope.launch {
            log("AccountsViewModel: getSelectedAccountId: ${getSelectedAccountId()}")
            val account = accountUseCases.getAccountById(getSelectedAccountId())
            log("AccountsViewModel: account: $account")
            onEvent(AccountEvent.SetSelectedAccount(account))
            onEvent(AccountEvent.LoadAccounts)
        }
    }
    private fun getSelectedAccountId(): Long {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.selectedAccountId, 1L)
                .first()
        }
    }
        fun onEvent(event: AccountEvent) {
        when (event) {
            is AccountEvent.SetAccountIBAN -> setAccountIBAN(event.iban)
            is AccountEvent.SetAccountBalance -> setAccountBalance(event.balance)
            is AccountEvent.SetAccountType -> setAccountType(event.accountType)
            is AccountEvent.SetAccountCategory -> setAccountCategory(event.accountCategory)
            is AccountEvent.SetTargetAccount -> setTargetAccount(
                event.accountId, event.accountIBAN, event.accountType, event.accountCategory,
                event.balance, event.currency
            )
            is AccountEvent.ShowAccountTypeSelectionDialog -> showAccountTypeDialog(event.show)
            is AccountEvent.ShowAccountCategorySelectionDialog -> showAccountCategoryDialog(event.show)
            is AccountEvent.ShowDeleteUpdateDialog -> showDeleteUpdateDialog(event.show)
            is AccountEvent.ShowDeleteConfirmationDialog -> showDeleteConfirmationDialog(event.show)
            is AccountEvent.InsertUpdateMode -> setUpdateMode(event.isUpdateMode)
            is AccountEvent.InsertOrUpdateAccount -> insertOrUpdateAccount(event.account)
            is AccountEvent.DeleteByAccountId -> deleteAccount()
            is AccountEvent.SetTempAccount -> setTempAccount(event.account)
            is AccountEvent.SetSelectedAccount -> setSelectedAccount(event.account)
            is AccountEvent.SetTempCurrency -> setTempCurrency(event.currency)
            AccountEvent.LoadAccounts -> loadAccounts()
        }
    }

    // ✅ PRIVATE HANDLER FUNCTIONS

    private fun updateSelectedAccountId(accountId:Long){
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(PreferenceKeys.selectedAccountId,accountId)
        }
    }
    private fun setAccountIBAN(iban: String) {
        _insertState.update { it.copy(accountIBAN = iban) }
    }

    private fun setAccountBalance(balance: String) {
        _insertState.update { it.copy(balance = balance) }
    }

    private fun setAccountType(type: String) {
        _insertState.update { it.copy(accountType = type) }
    }

    private fun setAccountCategory(category: String) {
        _insertState.update { it.copy(accountCategory = category) }
    }

    private fun setTargetAccount(
        accountId: Long?= null,
        accountIBAN: String = "",
        accountType: String = AccountType.Cash.name,
        accountCategory: String = AccountCategory.Personal.name,
        balance: String = "",
        currency: String = "PKR"
    ) {
        _insertState.update {
            it.copy(
                accountId = accountId,
                accountIBAN = accountIBAN,
                accountType = accountType,
                accountCategory = accountCategory,
                balance= balance,
                currency = currency
            )
        }
    }

    private fun showAccountTypeDialog(show: Boolean) {
        _insertState.update { it.copy(showAccountTypeSelectionDialog = show) }
    }

    private fun showAccountCategoryDialog(show: Boolean) {
        _insertState.update { it.copy(showAccountCategorySelectionDialog = show) }
    }

    private fun showDeleteUpdateDialog(show: Boolean) {
        _accountState.update { it.copy(showUpdateDeleteDialog = show) }
    }

    private fun showDeleteConfirmationDialog(show: Boolean) {
        _accountState.update { it.copy(showDeleteConfirmationDialog = show) }
    }

    private fun setUpdateMode(update: Boolean) {
        _insertState.update { it.copy(isUpdateMode = update) }
    }

    private fun setTempAccount(account: AccountModel?) {
        _accountState.update {
            it.copy(
                tempAccount = account
            )
        }
    }

    private fun setSelectedAccount(account: AccountModel?) {
        _accountState.update {
            it.copy(
                selectedAccount = account
            )
        }
        updateSelectedAccountId(accountId = account?.accountId?:1L)
    }

    private fun setTempCurrency(currency: String) {
        _insertState.update {
            it.copy(
                tempCurrency = currency
            )
        }
    }

    private fun insertOrUpdateAccount(account: AccountModel) {
        viewModelScope.launch {
            try {
                if (insertState.value.isUpdateMode) {
                    accountUseCases.updateAccount(account)
                } else {
                    accountUseCases.insertAccount(account)
                }
                _eventFlow.emit(
                    UiEvent.Saved(
                        if (insertState.value.isUpdateMode) "Account Updated Successfully!"
                        else "Account Saved Successfully!"
                    )
                )
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Couldn't Insert/Update Account!"))
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            try {
                _insertState.value.accountId?.let {
                    accountUseCases.deleteAccountById(it)
                    _eventFlow.emit(UiEvent.ShowToast("Account Deleted"))
                }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Couldn't Delete Account!"))
            }
        }
    }
    private fun loadAccounts() {
        viewModelScope.launch {
            _accountState.update { it.copy(isLoading = true, error = null) }
            try {
                accountUseCases.getAllAccounts()
                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
                    .collect { accounts ->
                        _accountState.update {
                            it.copy(accountsList = accounts, isLoading = false, error = null)
                        }
                    }
            } catch (e: Exception) {
                _accountState.update {
                    it.copy(isLoading = false, error = e.message ?: "Failed to load accounts")
                }
            }
        }
    }
}