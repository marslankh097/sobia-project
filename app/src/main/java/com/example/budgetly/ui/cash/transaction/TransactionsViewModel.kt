package com.example.budgetly.ui.cash.transaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.data.local.datasources.data_store.PreferenceKeys
import com.example.budgetly.domain.models.db.transactions.AccountModel
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.models.enums.sort.OrderBy
import com.example.budgetly.domain.models.enums.sort.SortBy
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.domain.models.enums.transaction.TransactionFrequency
import com.example.budgetly.domain.models.enums.transaction.TransactionScreenSource
import com.example.budgetly.domain.models.enums.transaction.TransactionType
import com.example.budgetly.domain.usecases.db_usecases.cash.AccountUseCases
import com.example.budgetly.domain.usecases.db_usecases.cash.CategoryUseCases
import com.example.budgetly.domain.usecases.db_usecases.cash.SubCategoryUseCases
import com.example.budgetly.domain.usecases.db_usecases.cash.TransactionUseCases
import com.example.budgetly.domain.usecases.system_usecases.PreferenceUseCases
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.ui.cash.filter.events.FilterEvent
import com.example.budgetly.ui.cash.filter.state.FilterDisplayState
import com.example.budgetly.ui.cash.sort.events.SortEvent
import com.example.budgetly.ui.cash.sort.state.SortDisplayState
import com.example.budgetly.ui.cash.transaction.events.GraphEvent
import com.example.budgetly.ui.cash.transaction.events.TransactionEvent
import com.example.budgetly.ui.cash.transaction.events.TransactionInsertEvent
import com.example.budgetly.ui.cash.transaction.state.TransactionDisplayState
import com.example.budgetly.ui.cash.transaction.state.TransactionGraphState
import com.example.budgetly.ui.cash.transaction.state.TransactionInsertState
import com.example.budgetly.utils.Utils.getTimeStampsFromDays
import com.example.budgetly.utils.log
import com.example.budgetly.utils.toDateString
import com.example.budgetly.utils.toSafeDouble
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionUseCases: TransactionUseCases,
    private val accountUseCases: AccountUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val subCategoryUseCases: SubCategoryUseCases,
    private val preferenceUseCases: PreferenceUseCases
) : ViewModel() {
    init {
        log("init: TransactionsViewModel")
        viewModelScope.launch {
            val account = accountUseCases.getAccountById(getSelectedAccountId())
            log("account:  accountUseCases.getAccountById(getSelectedAccountId()): $account")
            val duration = TransactionDuration.entries.find { it.name == getSelectedDuration() }
            val sortBy = SortBy.entries.find { it.name == getSortBy() }
            val orderBy = OrderBy.entries.find { it.name == getOrderBy() }
            duration?.let {
                onEvent(FilterEvent.SetTransactionDuration(it))
                val (from, to) = getFromTo(duration)
                onEvent(FilterEvent.SetTimeStamp(from, to))
            }
            sortBy?.let {
                onEvent(SortEvent.SetSortBy(it))
            }
            orderBy?.let {
                onEvent(SortEvent.SetOrderBy(it))
            }
            account?.let {
                onEvent(TransactionEvent.SetDisplayAccount(account))
            }
            onEvent(TransactionEvent.LoadTransactions)
            onEvent(GraphEvent.LoadTransactionGraph)
            observeAccountBalance()
        }
    }


    //    sorting code

    private val _filterState = MutableStateFlow(FilterDisplayState())
    val filterState: StateFlow<FilterDisplayState> = _filterState.asStateFlow()

    fun onEvent(event:FilterEvent){
        when(event){
            is FilterEvent.SetScreenSource -> setScreenSource(event.source)
            is FilterEvent.SetTempFromTimeStamp -> setTempFromTimeStamp(event.timeStamp)
            is FilterEvent.SetTempToTimeStamp -> setTempToTimeStamp(event.timeStamp)
            is FilterEvent.SetTimeStamp -> setTimeStamp(event.fromTimeStamp, event.toTimeStamp)
            is FilterEvent.SetTransactionDuration -> setTransactionDuration(event.transactionDuration)
            is FilterEvent.ShowDurationSelectionDialog -> showDurationSelectionDialog(event.show)
            is FilterEvent.ShowFromCalendarDialog -> showFromCalenderDialog(event.show)
            is FilterEvent.ShowToCalendarDialog -> showToCalenderDialog(event.show)
            is FilterEvent.UseAmountRange -> useAmountRange(event.use)
            is FilterEvent.UseCustomDateRange -> useCustomDateRange(event.use)
        }
    }

    private fun useCustomDateRange(use:Boolean) {
        _filterState.update {
            it.copy(
                useCustomDateRange = use
            )
        }
    }
    private fun showDurationSelectionDialog(use:Boolean) {
        _filterState.update {
            it.copy(
                showDurationSelectionDialog = use
            )
        }
    }
    private fun useAmountRange(use:Boolean) {
        _filterState.update {
            it.copy(
                useAmountRange = use
            )
        }
    }

    private fun setTempFromTimeStamp(timeStamp:Long) {
        _filterState.update {
            it.copy(
                tempFromTimeStamp = timeStamp
            )
        }
    }
    private fun setTempToTimeStamp(timeStamp: Long) {
        _filterState.update {
            it.copy(
                tempToTimeStamp = timeStamp
            )
        }
    }

    private fun setScreenSource(source: String) {
        _filterState.update {
            it.copy(
                screenSource = source
            )
        }
        displayCategorySubCategoryAccount()
    }
    private fun displayCategorySubCategoryAccount(){
       val (category, subcategory, account) =  when(TransactionScreenSource.valueOf(filterState.value.screenSource)){
            TransactionScreenSource.All -> Triple(true, true, true)
            TransactionScreenSource.Category -> Triple(false, true, false)
            TransactionScreenSource.SubCategory -> Triple(false, false, false)
            TransactionScreenSource.Expense -> Triple(true, true, false)
            TransactionScreenSource.Income -> Triple(true, true, false)
        }
        onEvent(TransactionInsertEvent.ShowCategorySubCategoryAccount(category,subcategory,account))
    }

    private fun getSelectedDuration(): String {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(
                PreferenceKeys.selectedDuration,
                TransactionDuration.OneMonth.name
            )
                .first()
        }
    }
    private fun getFromTimeStamp(): Long {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(
                PreferenceKeys.fromTimeStamp, 0L
            ).first()
        }
    }
    private fun getToTimeStamp(): Long {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(
                PreferenceKeys.toTimeStamp, 0L
            ).first()
        }
    }

    private fun updateSelectedDuration(duration: String) {
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(
                PreferenceKeys.selectedDuration,
                duration
            )
        }
    }
    private fun updateFromTimeStamp(timeStamp: Long) {
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(
                PreferenceKeys.fromTimeStamp,
                timeStamp
            )
        }
    }
    private fun updateToTimeStamp(timeStamp: Long) {
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(
                PreferenceKeys.toTimeStamp,
                timeStamp
            )
        }
    }
    private fun setTransactionDuration(duration: TransactionDuration) {
        _filterState.update {
            it.copy(
                transactionDuration = duration
            )
        }
        updateSelectedDuration(duration.name)
    }

    private fun setTimeStamp(fromTimeStamp: Long, toTimeStamp: Long) {
        _filterState.update {
            it.copy(
                fromTimeStamp = fromTimeStamp,
                toTimeStamp = toTimeStamp
            )
        }
        updateFromTimeStamp(fromTimeStamp)
        updateToTimeStamp(toTimeStamp)
    }


    private fun showFromCalenderDialog(show: Boolean) {
        _filterState.update { it.copy(showFromCalenderDialog = show) }
    }
    private fun showToCalenderDialog(show: Boolean) {
        _filterState.update { it.copy(showToCalenderDialog = show) }
    }


    //    ..............................////...........................////

    //    sorting code
    private val _sortState = MutableStateFlow(SortDisplayState())
    val sortState: StateFlow<SortDisplayState> = _sortState.asStateFlow()
    fun onEvent(event: SortEvent){
        when(event){
            is SortEvent.ShowSortingDialog -> showSortingDialog(event.show)
            is SortEvent.SetOrderBy ->setOrderBy(event.orderBy)
            is SortEvent.SetSortBy -> setSortBy(event.sortBy)
            is SortEvent.SetTempOrderBy -> setTempOrderBy(event.orderBy)
            is SortEvent.SetTempSortBy -> setTempSortBy(event.sortBy)
        }
    }
    private fun getSortBy(): String {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(
                PreferenceKeys.sortBy,
                SortBy.DateCreated.name
            )
                .first()
        }
    }
    private fun getOrderBy(): String {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(
                PreferenceKeys.orderBy,
                OrderBy.Descending.name
            )
                .first()
        }
    }

    private fun updateSortBy (sortBy: String) {
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(
                PreferenceKeys.sortBy,
                sortBy
            )
        }
    }
    private fun updateOrderBy (orderBy: String) {
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(
                PreferenceKeys.orderBy,
                orderBy
            )
        }
    }
    private fun setTempOrderBy(orderBy: OrderBy) {
        _sortState.update {
            it.copy(
                tempOrderBy = orderBy
            )
        }
    }
    private fun setTempSortBy(sortBy: SortBy) {
        _sortState.update {
            it.copy(
                tempSortBy = sortBy
            )
        }
    }
    private fun setOrderBy(orderBy: OrderBy) {
        _sortState.update { it.copy(orderBy = orderBy) }
        updateOrderBy(orderBy.name)

    }
    private fun setSortBy(sortBy: SortBy) {
        _sortState.update { it.copy(sortBy = sortBy) }
        updateSortBy(sortBy.name)
    }
    private fun showSortingDialog(show: Boolean) {
        _sortState.update { it.copy(showSortingDialog = show) }
    }


//    ..............................////...........................////
    //    ui event flow code

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()
    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ShowToast -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowToast(event.message))
                }
            }

            is UiEvent.Saved -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.Saved(event.message))
                }
            }
        }
    }

    //    ..............................////...........................////

    //    transaction graph code
    private val _graphState = MutableStateFlow(TransactionGraphState())
    val graphState: StateFlow<TransactionGraphState> = _graphState.asStateFlow()

    fun onEvent(event: GraphEvent){
        when(event){
            GraphEvent.LoadTransactionGraph -> loadTransactionGraph()
            is GraphEvent.ChangeGraphType -> {
                _graphState.update { it.copy(isBar = !it.isBar) }
            }
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadTransactionGraph() {
        viewModelScope.launch {
            combine(
                _state.map { it.accountId ?: 1L }.distinctUntilChanged(),
                _state.map { it.categoryId }.distinctUntilChanged(),
                _state.map { it.subcategoryId }.distinctUntilChanged(),
                _filterState.map { it.screenSource }.distinctUntilChanged(),
            ) { _, _, _ ,_-> Unit }
                .onEach {
                    _graphState.update { it.copy(isLoading = true, error = null) }
                }
                .launchIn(this)
            combine(
                _state.map { it.accountId ?: 1L },
                _state.map { it.categoryId },
                _state.map { it.subcategoryId },
                _filterState.map { it.transactionDuration.days },
                _filterState.map { it.fromTimeStamp },
                _filterState.map { it.toTimeStamp },
                _filterState.map { it.screenSource },
            ) { values: Array<Any?> ->
                TransactionQueryParams(
                    accountId = values[0] as Long,
                    categoryId = values[1] as Long?,
                    subcategoryId = values[2] as Long?,
                    days = values[3] as Int,
                    fromTimeStamp = values[4] as Long,
                    toTimeStamp = values[5] as Long,
                    source = values[6] as String,
                    sortBy = sortState.value.sortBy.name,
                    orderBy = sortState.value.orderBy.name
                )
            }
                .distinctUntilChanged()
                .flatMapLatest { params ->
                    transactionUseCases.getAllTransactionsByAccountId(params.accountId)
                        .map { txs ->
                            val (from, to) = if (params.days == 0) {
                                params.fromTimeStamp to params.toTimeStamp
                            } else {
                                getTimeStampsFromDays(params.days)
                            }
                            val rangeFiltered = txs.filter { it.date in from..to }

                            val filtered = when (TransactionScreenSource.valueOf(params.source)) {
                                TransactionScreenSource.All -> rangeFiltered
                                TransactionScreenSource.Category -> params.categoryId?.let { id ->
                                    rangeFiltered.filter { it.categoryId == id }
                                } ?: emptyList()

                                TransactionScreenSource.SubCategory -> params.subcategoryId?.let { id ->
                                    rangeFiltered.filter { it.subcategoryId == id }
                                } ?: emptyList()

                                TransactionScreenSource.Expense -> rangeFiltered.filter { it.type == TransactionType.Expense.name }
                                TransactionScreenSource.Income -> rangeFiltered.filter { it.type == TransactionType.Income.name }
                            }

                            // Group for graph points (date -> total amount)
                            val byDate = filtered
                                .groupBy { it.date.toDateString("yyyy-MM-dd") }
                                .mapValues { (_, list) ->
                                    list.sumOf {
                                        it.amount.toDoubleOrNull() ?: 0.0
                                    }
                                }
                                .toSortedMap()

                            byDate.toList()
                        }
                }.onStart {
                    _graphState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _graphState.update {
                        it.copy(
                            isLoading = false,
                            error = e.localizedMessage ?: "Failed to load graph data"
                        )
                    }
                }
                .collect { graphPoints ->
                    _graphState.update {
                        it.copy(graphPoints = graphPoints, isLoading = false, error = null)
                    }
                    updateGraphPoints(graphPoints)
                }
        }
    }
    private fun updateGraphPoints(
        points: List<Pair<String, Double>>,
    ) {
        if (points.isNotEmpty()) {
            viewModelScope.launch {
                _graphState.value.barModelProducer.runTransaction {
                    columnSeries { series(points.map { it.second.toFloat() }) }
                }
                _graphState.value.lineModelProducer.runTransaction {
                    lineSeries { series(points.map { it.second.toFloat() }) }
                }
            }
        }
    }

    //    ..............................////...........................////

    //transaction insert code

    private val _insertState = MutableStateFlow(TransactionInsertState())
    val insertState: StateFlow<TransactionInsertState> = _insertState.asStateFlow()

    fun onEvent(event:TransactionInsertEvent){
        when(event){
            is TransactionInsertEvent.SetTransactionAmount -> {
                _insertState.value = insertState.value.copy(amount = event.amount)
            }

            is TransactionInsertEvent.SetTransactionCurrency -> {
                _insertState.value = insertState.value.copy(currency = event.currency)
            }

            is TransactionInsertEvent.SetTransactionFrequency -> {
                _insertState.value = insertState.value.copy(frequency = event.frequency)
            }

            is TransactionInsertEvent.SetTransactionNotes -> {
                _insertState.value = insertState.value.copy(notes = event.notes)
            }

            is TransactionInsertEvent.SetTransactionType -> {
                _insertState.value = insertState.value.copy(type = event.type)
            }

            is TransactionInsertEvent.SetAccount -> {
                setAccount(event.account)
            }

            is TransactionInsertEvent.SetCategory -> {
                _insertState.value = insertState.value.copy(
                    categoryId = event.category?.categoryId,
                    categoryModel = event.category
                )
            }

            is TransactionInsertEvent.SetSubCategory -> {
                _insertState.value = insertState.value.copy(
                    subcategoryId = event.subCategory?.subCategoryId,
                    subCategoryModel = event.subCategory
                )
            }

            is TransactionInsertEvent.ShowFrequencySelectionDialog -> {
                _insertState.value =
                    insertState.value.copy(showFrequencySelectionDialog = event.show)
            }

            is TransactionInsertEvent.InsertOrUpdateTransaction -> {
                insertOrUpdateTransaction()
            }
            is TransactionInsertEvent.InsertUpdateMode -> setInsertUpdateMode(event.isUpdate)
            is TransactionInsertEvent.SetTempCurrency -> setTempCurrency(event.currency)
            is TransactionInsertEvent.SetTempCategory -> setTempCategory(event.category)
            is TransactionInsertEvent.SetTempSubCategory -> setTempSubCategory(event.subCategory)
            is TransactionInsertEvent.DisplayCategorySelection -> displayCategorySelection(event.show)
            is TransactionInsertEvent.DisplaySubCategorySelection -> displaySubCategorySelection(event.show)
            is TransactionInsertEvent.SetCategorySubCategoryAccount -> setCategorySubCategoryAccount(
                event.category,
                event.subCategory,
                event.account
            )
            is TransactionInsertEvent.ShowCategorySubCategoryAccount -> showCategorySubCategoryAccount(
                event.displayCategorySelection,
                event.displaySubCategorySelection,
                event.displayAccountSelection
            )

            is TransactionInsertEvent.SetSelectCategory -> setSelectCategory(event.selectCategory)
            is TransactionInsertEvent.PrepareInsertUpdate -> prepareInsertUpdate(
                transactionId = event.transactionId,
                amount = event.amount, currency = event.currency,
                notes = event.notes, frequency = event.frequency, event.dateTime
//                type = event.type
            )



            is TransactionInsertEvent.SetCategoryById -> {
                viewModelScope.launch {
                    event.id?.let { id ->
                        val category = categoryUseCases.getCategoryById(id)
                        onEvent(TransactionInsertEvent.SetCategory(category))
                    }
                }
            }

            is TransactionInsertEvent.SetTransactionDateTime -> {
                _insertState.update { it.copy(dateTime = event.dateTime) }
            }


            is TransactionInsertEvent.SetSubCategoryById -> {
                viewModelScope.launch {
                    event.id?.let { id ->
                        val subCategory = subCategoryUseCases.getSubCategoryById(id)
                        onEvent(TransactionInsertEvent.SetSubCategory(subCategory))
                    }
                }
            }
            is TransactionInsertEvent.SetAccountById -> {
                viewModelScope.launch {
                    event.id?.let { id ->
                        val account = accountUseCases.getAccountById(id)
                        onEvent(TransactionInsertEvent.SetAccount(account))
                    }
                }
            }
            is TransactionInsertEvent.DisplayAccountSelection -> displayAccountSelection(event.show)
            is TransactionInsertEvent.SetTempAccount -> setTempAccount(event.account)
        }
    }
    private fun setAccount(account: AccountModel?) {
        _insertState.update {
            it.copy(
                accountId = account?.accountId,
                accountModel = account
            )
        }
    }
    private fun setCategory(category: CategoryModel?) {
        _insertState.update {
            it.copy(
                categoryId = category?.categoryId,
                categoryModel = category
            )
        }
    }
    private fun setSubCategory(subcategory: SubCategoryModel?) {
        _insertState.update {
            it.copy(
                subcategoryId = subcategory?.subCategoryId,
                subCategoryModel = subcategory
            )
        }
    }

    private fun setTempCategory(categoryModel: CategoryModel?) {
        _insertState.update { it.copy(tempCategory = categoryModel) }
    }

    private fun setTempSubCategory(subCategoryModel: SubCategoryModel?) {
        _insertState.update { it.copy(tempSubCategory = subCategoryModel) }
    }
    private fun setTempAccount(account: AccountModel?) {
        _insertState.update { it.copy(accountModel = account) }
    }


    private fun setTempCurrency(currency: String = "PKR") {
        _insertState.update { it.copy(tempCurrency = currency) }
    }
    private fun displayCategorySelection(show: Boolean) {
        _insertState.update { it.copy(displayCategorySelection = show) }
    }

    private fun setInsertUpdateMode(isUpdate: Boolean) {
        _insertState.update { it.copy(isUpdateMode = isUpdate) }
    }

    private fun setCategorySubCategoryAccount(
        categoryModel: CategoryModel?,
        subCategoryModel: SubCategoryModel?,
        accountModel: AccountModel?,
    ) {
        log("setCategorySubCategoryAccount: $categoryModel, $subCategoryModel $accountModel")
        _insertState.update {
            it.copy(
                categoryId = categoryModel?.categoryId,
                subcategoryId = subCategoryModel?.subCategoryId,
                categoryModel = categoryModel ?:insertState.value.categoryModel,
                accountId =  accountModel?.accountId,
                accountModel = accountModel ?:insertState.value.accountModel,
                subCategoryModel = subCategoryModel?:insertState.value.subCategoryModel
            )
        }
    }
    private fun showCategorySubCategoryAccount(
        displayCategorySelection: Boolean,
        displaySubCategorySelection: Boolean,
        displayAccountSelection: Boolean
    ) {
        _insertState.update {
            it.copy(
                displayCategorySelection = displayCategorySelection,
                displaySubCategorySelection = displaySubCategorySelection,
                displayAccountSelection = displayAccountSelection
            )
        }
    }

    private fun displaySubCategorySelection(show: Boolean) {
        _insertState.update { it.copy(displaySubCategorySelection = show) }
    }
    private fun displayAccountSelection(show: Boolean) {
        _insertState.update { it.copy(displayAccountSelection = show) }
    }

    private fun setSelectCategory(selectCategory: Boolean) {
        _insertState.update {
            it.copy(
                selectCategory = selectCategory
            )
        }
    }

    private fun insertOrUpdateTransaction() {
        val state = insertState.value
//        if(state.accountId != null && state.categoryId != null && state.subcategoryId != null){
        try {
            val transaction = TransactionModel(
                accountId = state.accountId,
                categoryId = state.categoryId,
                subcategoryId = state.subcategoryId,
                transactionId = state.transactionId ?: 0L,
                amount = state.amount,
                currency = state.currency,
                description = state.notes,
                frequency = state.frequency,
                type = state.type,
//                date = System.currentTimeMillis()
                date = state.dateTime
            )
            if (transaction.amount.trim().isEmpty()
            ) throw IllegalArgumentException("Transaction Amount can't be empty")
            if (transaction.amount.trim().toSafeDouble() == 0.0)
                throw IllegalArgumentException("Transaction Amount can't be zero")
            viewModelScope.launch {
                if (state.isUpdateMode) {
                    transactionUseCases.updateTransaction(transaction)
                } else {
                    transactionUseCases.insertTransaction(transaction)
                }
                _eventFlow.emit(
                    UiEvent.Saved(
                        if (state.isUpdateMode) {
                            "Transaction Updated Successfully!"
                        } else {
                            "Transaction Saved Successfully!"
                        }
                    )
                )
                log("transaction.date:${transaction.date}")
                // Optionally reset state after operation
                prepareInsertUpdate()
            }
        } catch (e: Exception) {
            log(
                e.localizedMessage
                    ?: if (state.isUpdateMode) "Couldn't Update Transaction" else "Couldn't Insert Transaction"
            )
            onEvent(
                UiEvent.ShowToast(
                    e.localizedMessage
                        ?: if (state.isUpdateMode) "Couldn't Update Transaction" else "Couldn't Insert Transaction"
                )
            )
        }
    }

    private fun prepareInsertUpdate(
        transactionId: Long? = null,
        amount: String = "",
        currency: String = "PKR",
        notes: String = "",
        frequency: String = TransactionFrequency.OneTime.name,
        dateTime: Long = System.currentTimeMillis()
//                                    type: String = TransactionFrequency.OneTime.name
    ) {
        _insertState.update {
            it.copy(
                transactionId = transactionId,
                amount = amount,
                currency = currency,
                notes = notes,
                frequency = frequency,
                dateTime = dateTime
//            type = type
            )
        }
    }

    //    ..............................////...........................////


    //transaction display code

    private val _state = MutableStateFlow(TransactionDisplayState())
    val state: StateFlow<TransactionDisplayState> = _state.asStateFlow()

    fun onEvent(event: TransactionEvent) {
        when (event) {
            TransactionEvent.LoadTransactions -> loadTransactions()

            is TransactionEvent.ShowDeleteUpdateDialog -> showDeleteUpdateDialog(event.show)
            is TransactionEvent.ShowDeleteConfirmationDialog -> showDeleteConfirmationDialog(event.show)
            is TransactionEvent.DeleteTransactionById -> {
                insertState.value.transactionId?.let {
                    deleteTransaction(it)
                }
            }
            is TransactionEvent.SetDisplayAccount -> setDisplayAccount(event.account)
            is TransactionEvent.SetDisplayAccountById -> {
                viewModelScope.launch {
                    event.id?.let { id ->
                        val account = accountUseCases.getAccountById(id)
                        onEvent(TransactionEvent.SetDisplayAccount(account))
                    }
                }
            }
            is TransactionEvent.SetDisplayCategory -> setDisplayCategory(event.category)
            is TransactionEvent.SetDisplayCategoryById -> {
                viewModelScope.launch {
                    event.id?.let { id ->
                        val category = categoryUseCases.getCategoryById(id)
                        onEvent(TransactionEvent.SetDisplayCategory(category))
                    }
                }
            }
            is TransactionEvent.SetDisplaySubCategory -> setDisplaySubCategory(event.subCategory)
            is TransactionEvent.SetDisplaySubCategoryById -> {
                viewModelScope.launch {
                    event.id?.let { id ->
                        val subCategory = subCategoryUseCases.getSubCategoryById(id)
                        onEvent(TransactionEvent.SetDisplaySubCategory(subCategory))
                    }
                }
            }

            is TransactionEvent.SetDisplayCategorySubCategoryAccount -> setDisplayCategorySubCategoryAccount(
                categoryModel = event.category,
                subCategoryModel = event.subCategory,
                accountModel = event.account
            )
        }
    }


    private fun setDisplayAccount(account: AccountModel?) {
        log("viewmodel: setDisplayAccount: $account", "Account")
        _state.update {
            it.copy(
                accountId = account?.accountId,
                accountModel = account
            )
        }
    }
    private fun setDisplayCategory(category: CategoryModel?) {
        _state.update {
            it.copy(
                categoryId = category?.categoryId,
                categoryModel = category
            )
        }
    }
    private fun setDisplaySubCategory(subcategory: SubCategoryModel?) {
        _state.update {
            it.copy(
                subcategoryId = subcategory?.subCategoryId,
                subCategoryModel = subcategory
            )
        }
    }
    private fun getSelectedAccountId(): Long {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.selectedAccountId, 1L)
                .first()
        }
    }

    fun updateSelectedAccountId(accountId: Long) {
        viewModelScope.launch {
            preferenceUseCases.updatePreferenceValue.invoke(
                PreferenceKeys.selectedAccountId,
                accountId
            )
        }
    }
    private fun showDeleteUpdateDialog(show: Boolean) {
        _state.update { it.copy(showUpdateDeleteDialog = show) }
    }

    private fun showDeleteConfirmationDialog(show: Boolean) {
        _state.update { it.copy(showDeleteConfirmationDialog = show) }
    }


    private fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            try {
                transactionUseCases.deleteTransactionById(transactionId)
                _eventFlow.emit(UiEvent.ShowToast("Transaction Deleted Successfully!"))
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Delete failed"))
            }
        }
    }

    private fun setDisplayCategorySubCategoryAccount(
        categoryModel: CategoryModel?,
        subCategoryModel: SubCategoryModel?,
        accountModel: AccountModel?
    ) {
        log("setDisplayCategorySubCategoryAccount: $categoryModel, $subCategoryModel $accountModel")
        _state.update {
            it.copy(
                categoryId = categoryModel?.categoryId,
                subcategoryId = subCategoryModel?.subCategoryId,
                categoryModel = categoryModel ?: state.value.categoryModel,
                accountId =  accountModel?.accountId,
                accountModel = accountModel?:state.value.accountModel,
                subCategoryModel = subCategoryModel?: state.value.subCategoryModel,
            )
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadTransactions() {
        viewModelScope.launch {
            combine(
                _state.map { it.accountId ?: 1L }.distinctUntilChanged(),
                _state.map { it.categoryId }.distinctUntilChanged(),
                _state.map { it.subcategoryId }.distinctUntilChanged(),
                _filterState.map { it.screenSource }.distinctUntilChanged(),
            ) { _, _, _ ,_-> Unit }
                .onEach {
                    _state.update { it.copy(transactions = emptyList(), isLoading = true, error = null) }
                }
                .launchIn(this)
            combine(
                _state.map { it.accountId ?: 1L }.distinctUntilChanged(),
                _state.map { it.categoryId }.distinctUntilChanged(),
                _state.map { it.subcategoryId }.distinctUntilChanged(),
                _filterState.map { it.transactionDuration.days }.distinctUntilChanged(),
                _filterState.map { it.fromTimeStamp }.distinctUntilChanged(),
                _filterState.map { it.toTimeStamp }.distinctUntilChanged(),
                _filterState.map { it.screenSource }.distinctUntilChanged(),
                _sortState.map { it.sortBy.name }.distinctUntilChanged(),
                _sortState.map { it.orderBy.name }.distinctUntilChanged(),
            ) { values: Array<Any?> ->
                TransactionQueryParams(
                    accountId = values[0] as Long,
                    categoryId = values[1] as Long?,
                    subcategoryId = values[2] as Long?,
                    days = values[3] as Int,
                    fromTimeStamp = values[4] as Long,
                    toTimeStamp = values[5] as Long,
                    source = values[6] as String,
                    sortBy =  values[7] as String,
                    orderBy =  values[8] as String,
                )
            }.distinctUntilChanged()
                .flatMapLatest { params ->
                    accountUseCases.getAccountByIdFlow(params.accountId).filterNotNull().flatMapLatest { account ->
                        log("startingBalance: ${account.initialBalance}", "Account")
                        val startingBalance = account.initialBalance.toDoubleOrNull() ?: 0.0
//                    val startingBalance =  _state.value.accountModel?.initialBalance?.toDoubleOrNull() ?: 0.0
                        transactionUseCases.getAllTransactionsByAccountId(params.accountId)
                            .map { transactions ->
                                val transactionsDelta = transactions.sumOf {
                                    when (it.type) {
                                        TransactionType.Income.name -> it.amount.toDoubleOrNull()
                                            ?: 0.0

                                        TransactionType.Expense.name -> -(it.amount.toDoubleOrNull()
                                            ?: 0.0)

                                        else -> 0.0
                                    }
                                }

                                val balance = startingBalance + transactionsDelta
                                log("balance: $balance", "Account")
                                onEvent(
                                    TransactionEvent.SetDisplayAccount(
//                                        state.value.accountModel?.copy(
//                                            balance = balance.toString()
//                                        )
                                        account.copy(
                                            balance = balance.toString()
                                        )
                                    )
                                )
                                val (from, to) = if (params.days == 0) {
                                    Pair(params.fromTimeStamp, params.toTimeStamp)
                                } else {
                                    getTimeStampsFromDays(params.days)
                                }
                                val list = transactions.filter { it.date in from..to }
                                params to list
                            }
                    }
                }
                .onStart {
                    _state.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.localizedMessage ?: "Failed to load transactions"
                        )
                    }
                }
                .collectLatest { (params, transactions) ->
                    val filtered = when (TransactionScreenSource.valueOf(params.source)) {
                        TransactionScreenSource.All -> transactions
                        TransactionScreenSource.Category -> params.categoryId?.let { id ->
                            transactions.filter { it.categoryId == id }
                        } ?: emptyList()

                        TransactionScreenSource.SubCategory -> params.subcategoryId?.let { id ->
                            transactions.filter { it.subcategoryId == id }
                        } ?: emptyList()

                        TransactionScreenSource.Expense -> transactions.filter { it.type == TransactionType.Expense.name }
                        TransactionScreenSource.Income -> transactions.filter { it.type == TransactionType.Income.name }
                    }
                    val sorted = when (SortBy.valueOf(params.sortBy)) {
                        SortBy.Amount -> {
                            if (OrderBy.valueOf(params.orderBy) == OrderBy.Ascending)
                                filtered.sortedBy { it.amount.toDoubleOrNull() ?: 0.0 }
                            else
                                filtered.sortedByDescending { it.amount.toDoubleOrNull() ?: 0.0 }
                        }

                        SortBy.DateCreated -> {
                            if (OrderBy.valueOf(params.orderBy) == OrderBy.Ascending)
                                filtered.sortedBy { it.date }
                            else
                                filtered.sortedByDescending { it.date }
                        }
                    }
                    _state.update {
                        it.copy(
                            transactions = sorted,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }

    private fun getAccountByIdFlow(id: Long): Flow<AccountModel> = flow {
        accountUseCases.getAccountById(id)
            ?.let { emit(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeAccountBalance() {
        viewModelScope.launch {
            _state.map { it.accountId ?: 1L }
                .distinctUntilChanged()
                .flatMapLatest { accountId -> getAccountByIdFlow(accountId) }
                .collectLatest { account ->
                    log("observeAccountBalance: ${account}", "Account")
                    onEvent(TransactionEvent.SetDisplayAccount(account))
                }
        }
    }
    private fun getFromTo(duration: TransactionDuration):Pair<Long,Long>{
        return if(duration == TransactionDuration.DateRange){
            val toTime = getToTimeStamp()
            val fromTime = getFromTimeStamp()
            if(toTime != 0L && fromTime != 0L){
                Pair(fromTime, toTime)
            }else{
                getTimeStampsFromDays(30)
            }
        }else{
            getTimeStampsFromDays(duration.days)
        }
    }
    data class TransactionQueryParams(
        val accountId: Long,
        val categoryId: Long?,
        val subcategoryId: Long?,
        val days: Int,
        val fromTimeStamp: Long,
        val toTimeStamp: Long,
        val source: String,
        val sortBy: String,
        val orderBy: String,
    )
}
