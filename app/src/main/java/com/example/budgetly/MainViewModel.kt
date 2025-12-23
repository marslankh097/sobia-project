package com.example.budgetly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.data.local.datasources.data_store.PreferenceKeys
import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.enums.category.CategoryType
import com.example.budgetly.domain.models.enums.category.CategoryUrgency
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.domain.usecases.db_usecases.cash.AccountUseCases
import com.example.budgetly.domain.usecases.db_usecases.cash.CategoryUseCases
import com.example.budgetly.domain.usecases.db_usecases.cash.SubCategoryUseCases
import com.example.budgetly.domain.usecases.system_usecases.PreferenceUseCases
import com.example.budgetly.utils.Utils
import com.example.budgetly.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceUseCases: PreferenceUseCases,
//    private val  remoteConfigUseCases: RemoteConfigUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val accountsUseCases: AccountUseCases,
    private val subCategoryUseCases: SubCategoryUseCases
) : ViewModel() {
    private val _requisitionId = MutableStateFlow<String?>(null)
    val requisitionId: StateFlow<String?> = _requisitionId

    private val _accounts = MutableStateFlow<List<AccountModel>>(emptyList())
    val accounts: StateFlow<List<AccountModel>> = _accounts

    init {
        insertPredefinedAccountIfEmpty()
        insertPredefinedCategoriesIfEmpty()
        observeAccounts()
    }
    private fun observeAccounts() {
        viewModelScope.launch {
            accountsUseCases.getAllAccounts().collect { accountList ->
                _accounts.value = accountList
            }
        }
    }
    fun setRequisitionId(id: String?) {
        _requisitionId.value = id
    }
    fun getPrefsDarkMode(): Boolean {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.isDarkMode,false).first()
        }
    }
    fun updatePrefsDarkMode(isDark:Boolean){
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(PreferenceKeys.isDarkMode,isDark)
        }
    }
    fun getAppThemePosition(): Int {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.appThemePosition,0).first()
        }
    }
//    fun getSplashTime(): Int {
//       return remoteConfigUseCases.getRemoteConfigValue.invoke(RemoteConfigKeys.splashTime,11)
//    }
    fun updateAppThemePosition(themePosition:Int){
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(PreferenceKeys.appThemePosition,themePosition)
        }
    }
    private fun insertPredefinedCategoriesIfEmpty() {
        viewModelScope.launch {
            val duration  = TransactionDuration.entries.find { it.name == getSelectedDuration() }
            val (from, to) = Utils.getTimeStampsFromDays(duration?.days?:30)
//            categoryUseCases.getAllCategories(getSelectedAccountId(), from, to).collect { existingCategories ->
            categoryUseCases.getAllCategories().collect { existingCategories ->
                if (existingCategories.isEmpty()) {
                    insertPredefinedCategoriesAndSubcategories()
                }
            }
        }
    }
    private fun insertPredefinedAccountIfEmpty() {
        viewModelScope.launch {
            accountsUseCases.getAllAccounts().collect { existingAccounts ->
                if (existingAccounts.isEmpty()) {
                    insertPredefinedAccount()
                }
            }
        }
    }
    private suspend fun insertPredefinedAccount() {
        val accountModel = AccountModel(
            accountIBAN = "12345678",
            balance = "0.0",
            initialBalance = "0.0",
            currency = "PKR"
        )
        log("mainViewModel: insertPredefinedAccount: $accountModel")
        accountsUseCases.insertAccount(accountModel)
        updateSelectedAccountId(accountId = accountModel.accountId)
    }
    private fun getSelectedAccountId(): Long {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.selectedAccountId, 1L)
                .first()
        }
    }
        private fun updateSelectedAccountId(accountId:Long){
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(PreferenceKeys.selectedAccountId,accountId)
        }
    }
    private fun getSelectedDuration(): String {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.selectedDuration, TransactionDuration.OneMonth.name)
                .first()
        }
    }
        private fun updateSelectedDuration(accountId:String){
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(PreferenceKeys.selectedDuration,accountId)
        }
    }
    private suspend fun insertPredefinedCategoriesAndSubcategories() {
        val expenseCategories = listOf(
            "Food & Drinks" to listOf(
                "Bar, Cafe" to CategoryUrgency.Want,
                "Groceries" to CategoryUrgency.Must,
                "Restaurant, Fast Food" to CategoryUrgency.Need
            ),
            "Shopping" to listOf(
                "Clothes & Shoes" to CategoryUrgency.Need,
                "Drug Store, Chemist" to CategoryUrgency.Must,
                "Electronics, Accessories" to CategoryUrgency.Want,
                "Free Time" to CategoryUrgency.Want,
                "Gifts, Joy" to CategoryUrgency.Want,
                "Health & Beauty" to CategoryUrgency.Need,
                "Home, Garden" to CategoryUrgency.Need,
                "Jewels, Accessories" to CategoryUrgency.Want,
                "Kids" to CategoryUrgency.Need,
                "Pets, Animals" to CategoryUrgency.Need,
                "Stationery, Tools" to CategoryUrgency.Need
            ),
            "Housing" to listOf(
                "Energy, Utilities" to CategoryUrgency.Must,
                "Maintenance, Repairs" to CategoryUrgency.Must,
                "Mortgage" to CategoryUrgency.Must,
                "Property Insurance" to CategoryUrgency.Must,
                "Rent" to CategoryUrgency.Must,
                "Services" to CategoryUrgency.Need
            ),
            "Transportation" to listOf(
                "Business Trips" to CategoryUrgency.Want,
                "Long Distance" to CategoryUrgency.Need,
                "Public Transport" to CategoryUrgency.Need,
                "Taxi" to CategoryUrgency.Need
            ),
            "Vehicle" to listOf(
                "Fuel" to CategoryUrgency.Must,
                "Leasing" to CategoryUrgency.Must,
                "Parking" to CategoryUrgency.Need,
                "Rentals" to CategoryUrgency.Want,
                "Vehicle Insurance" to CategoryUrgency.Must,
                "Vehicle Maintenance" to CategoryUrgency.Must
            ),
            "Life & Entertainment" to listOf(
                "Active Support, Fitness" to CategoryUrgency.Need,
                "Alcohol, Tobacco" to CategoryUrgency.Want,
                "Books, Audio, Subscriptions" to CategoryUrgency.Want,
                "Charity, Gifts" to CategoryUrgency.Want,
                "Culture, Sports Events" to CategoryUrgency.Want,
                "Education, Development" to CategoryUrgency.Need,
                "Healthcare, Doctors" to CategoryUrgency.Must,
                "Hobbies" to CategoryUrgency.Want,
                "Holiday, Trips, Hotels" to CategoryUrgency.Want,
                "Life Events, Birthdays, Parties" to CategoryUrgency.Want,
                "Lottery, Gambling" to CategoryUrgency.Want,
                "TV, Streaming" to CategoryUrgency.Want,
                "Wellness, Beauty" to CategoryUrgency.Need
            ),
            "Communication, PC" to listOf(
                "Internet" to CategoryUrgency.Must,
                "Phone, Cell Phone" to CategoryUrgency.Must,
                "Postal Services" to CategoryUrgency.Need,
                "Softwares, Apps, Games" to CategoryUrgency.Want
            ),
            "Financial Expenses" to listOf(
                "Advisory" to CategoryUrgency.Need,
                "Charges, Fees" to CategoryUrgency.Must,
                "Child Support" to CategoryUrgency.Must,
                "Fines" to CategoryUrgency.Must,
                "Insurances" to CategoryUrgency.Must,
                "Loan, Interests" to CategoryUrgency.Must,
                "Taxes" to CategoryUrgency.Must
            ),
            "Investments" to listOf(
                "Collections" to CategoryUrgency.Want,
                "Financial Investments" to CategoryUrgency.Need,
                "Realty" to CategoryUrgency.Need,
                "Saving" to CategoryUrgency.Must,
                "Vehicles, Chattels" to CategoryUrgency.Need
            ),
//            "Income" to listOf(
//                "Checks, Coupons" to CategoryUrgency.Must,
//                "Child Support" to CategoryUrgency.Must,
//                "Dues, Grants" to CategoryUrgency.Must,
//                "Gifts" to CategoryUrgency.Want,
//                "Interest, Dividends" to CategoryUrgency.Must,
//                "Lending, Renting" to CategoryUrgency.Need,
//                "Lottery, Gambling" to CategoryUrgency.Want,
//                "Refunds (Tax, Purchase)" to CategoryUrgency.Must,
//                "Rental Income" to CategoryUrgency.Must,
//                "Sale" to CategoryUrgency.Must,
//                "Wage, Invoices" to CategoryUrgency.Must
//            ),

            "Other" to listOf(
                "Missing" to CategoryUrgency.Need
            )
        )
        val incomeCategories = mapOf(
            "Salary & Wages" to listOf(
                "Monthly Salary" to "",
                "Hourly Wages" to "",
                "Overtime Pay" to "",
                "Bonuses" to "",
                "Commissions" to ""
            ),
            "Business & Self-Employment" to listOf(
                "Freelance Income" to "",
                "Consulting" to "",
                "Business Profits" to "",
                "Side Hustles" to "",
                "Contract Work" to ""
            ),
            "Investments" to listOf(
                "Dividends" to "",
                "Interest Income" to "",
                "Capital Gains" to "",
                "Mutual Funds" to "",
                "Stock Earnings" to ""
            ),
            "Rental & Lending" to listOf(
                "Property Rental" to "",
                "Equipment Rental" to "",
                "Loan Repayments" to "",
                "Lease Income" to ""
            ),
            "Government & Support" to listOf(
                "Unemployment Benefits" to "",
                "Social Security" to "",
                "Child Support" to "",
                "Disability Benefits" to "",
                "Pension" to ""
            ),
            "Gifts & Grants" to listOf(
                "Monetary Gifts" to "",
                "Inheritance" to "",
                "Scholarships" to "",
                "Grants" to "",
                "Donations Received" to ""
            ),
            "Reimbursements & Refunds" to listOf(
                "Tax Refunds" to "",
                "Purchase Refunds" to "",
                "Medical Reimbursements" to "",
                "Travel Reimbursements" to ""
            ),
            "Windfalls & Others" to listOf(
                "Lottery Winnings" to "",
                "Gambling Wins" to "",
                "Scratch Cards" to "",
                "Insurance Payout" to "",
                "One-Time Settlement" to ""
            ),
            "Coupons & Discounts" to listOf(
                "Gift Cards" to "",
                "Cashback" to "",
                "Store Credits" to "",
                "Loyalty Rewards" to ""
            )
        )
        for ((categoryName, subCategories) in expenseCategories) {
            val category = CategoryModel(
                categoryName = categoryName,
                predefined = true
            )
            val categoryId = categoryUseCases.insertCategory(category)
            val subCategoryModels = subCategories.map { (subName, urgency) ->
                SubCategoryModel(
                    subCategoryId = 0L,
                    categoryId = categoryId,
                    subCategoryName = subName,
                    urgency = urgency.name,
                    predefined = true
                )
            }
            subCategoryUseCases.insertSubCategories(subCategoryModels)
        }
        for ((categoryName, subCategories) in incomeCategories) {
            val category = CategoryModel(
                categoryName = categoryName,
                categoryType = CategoryType.Income.name,
                predefined = true
            )
            val categoryId = categoryUseCases.insertCategory(category)
            val subCategoryModels = subCategories.map { (subName, _) ->
                SubCategoryModel(
                    subCategoryId = 0L,
                    categoryId = categoryId,
                    subCategoryName = subName,
                    predefined = true
                )
            }
            subCategoryUseCases.insertSubCategories(subCategoryModels)
        }
    }
}