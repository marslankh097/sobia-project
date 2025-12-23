package com.example.budgetly.ui.cash.category
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.data.local.datasources.data_store.PreferenceKeys
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.db.transactions.TransactionModel
import com.example.budgetly.domain.models.enums.category.CategoryType
import com.example.budgetly.domain.models.enums.transaction.TransactionDuration
import com.example.budgetly.domain.usecases.db_usecases.cash.CategoryUseCases
import com.example.budgetly.domain.usecases.db_usecases.cash.SubCategoryUseCases
import com.example.budgetly.domain.usecases.db_usecases.cash.TransactionUseCases
import com.example.budgetly.domain.usecases.system_usecases.PreferenceUseCases
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.ui.cash.category.state.CategoryDisplayState
import com.example.budgetly.ui.cash.category.state.CategoryInsertState
import com.example.budgetly.ui.cash.home.state.PieChartDetailState
import com.example.budgetly.ui.cash.home.state.PieChartState
import com.example.budgetly.ui.cash.home.state.PieSlice
import com.example.budgetly.utils.Utils
import com.example.budgetly.utils.Utils.getTimeStampsFromDays
import com.example.budgetly.utils.log
import com.example.budgetly.utils.toSafeDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val subCategoryUseCases: SubCategoryUseCases,
    private val preferenceUseCases: PreferenceUseCases
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _state = MutableStateFlow(CategoryDisplayState())
    val state: StateFlow<CategoryDisplayState> = _state.asStateFlow()

    private val _insertState = MutableStateFlow(CategoryInsertState())
    val insertState: StateFlow<CategoryInsertState> = _insertState.asStateFlow()

    private val _pieChartState = MutableStateFlow(PieChartState())
    val pieChartState: StateFlow<PieChartState> = _pieChartState.asStateFlow()

    private val _pieChartParentCategoryId = MutableStateFlow<Long?>(null)
     val pieChartParentCategoryId: StateFlow<Long?> = _pieChartParentCategoryId.asStateFlow()

    private val _isPieChartExpense = MutableStateFlow<Boolean>(true)
    val isPieChartExpense: StateFlow<Boolean> = _isPieChartExpense.asStateFlow()

    private val _categoryPieChartState = MutableStateFlow(PieChartDetailState())
    val categoryPieChartState: StateFlow<PieChartDetailState> = _categoryPieChartState.asStateFlow()

    private val _subcategoryPieChartState = MutableStateFlow(PieChartDetailState())
    val subcategoryPieChartState: StateFlow<PieChartDetailState> = _subcategoryPieChartState.asStateFlow()



    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observePieChartData() {
        viewModelScope.launch {
            val categoriesFlow = categoryUseCases.getAllCategories()
            val transactionsByCategoryFlow = _state
                .map { it.accountId ?: 1L }
                .distinctUntilChanged()
                .flatMapLatest { accountId ->  categoryUseCases.getTransactionByCategory(accountId) }
            combine(
                categoriesFlow,
                transactionsByCategoryFlow,
                _state.map { it.fromTimeStamp },
                _state.map { it.toTimeStamp },
                _isPieChartExpense
            ){ allCategories, transactionsByCat, fromTs, toTs, isExpense ->
                val params = PieChartParams(allCategories, transactionsByCat, fromTs, toTs, isExpense)
//            }.distinctUntilChanged().flatMapLatest{ params->
                val filteredCatMap = params.transactionsByCat.mapValues { (_, txs) ->
                    val (resolvedFrom, resolvedTo) = if (params.fromTs != null && params.toTs != null) {
                        params.fromTs to params.toTs
                    } else {
                        val duration = TransactionDuration.entries.find { it.name == getSelectedDuration() }
                        getFromTo(duration?:TransactionDuration.OneMonth)
                    }
                    txs.filter { it.date in resolvedFrom..resolvedTo }
                }
                val totalByCategory = filteredCatMap.mapValues { (_, txs) -> txs.sumOf { it.amount.toSafeDouble() } }

                // Split into income and expense
                val expenseCategories = params.allCategories.filter { it.categoryType == CategoryType.Expense.name }
                val incomeCategories = params.allCategories.filter { it.categoryType == CategoryType.Income.name }

                val totalExpense = expenseCategories.sumOf { totalByCategory[it.categoryId] ?: 0.0 }
                val totalIncome = incomeCategories.sumOf { totalByCategory[it.categoryId] ?: 0.0 }

                val expenseSlices = expenseCategories.mapToSlices(totalByCategory,totalExpense)
                val incomeSlices = incomeCategories.mapToSlices(totalByCategory, totalIncome)
                PieChartState(
                     expenseSlices = expenseSlices,
                     incomeSlices = incomeSlices,
                     isLoading = false,
                     error = null
                 ) to PieChartDetailState(
                     screenTitle =  "",
                     slices = if(params.isExpense) expenseSlices else incomeSlices,
                     isLoading = false,
                     error = null
                 )
            }.onStart {
                _pieChartState.update { it.copy(isLoading = true, error = null) }
                _categoryPieChartState.update { it.copy(isLoading = true, error = null) }
            }.catch { e->
                _pieChartState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                _categoryPieChartState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }.collectLatest { (state, detailState)->
                _pieChartState.update { state }
                _categoryPieChartState.update { detailState }
                log("_pieChartState:incomeSlices: ${_pieChartState.value.incomeSlices}\nexpenseSlices: ${_pieChartState.value.expenseSlices}")
                log("_categoryPieChartState:${_categoryPieChartState.value.slices}")
            }
        }
    }

    private val _tempParentCategory = MutableStateFlow<Pair<Long?,String>>(Pair( null, "None"))
    val tempParentCategory: StateFlow<Pair<Long?,String>> = _tempParentCategory.asStateFlow()


    private val _expandedCategories = MutableStateFlow<List<Long>>(mutableListOf())
    val expandedCategories: StateFlow<List<Long>> = _expandedCategories.asStateFlow()
    private val _incomeExpandedCategories = MutableStateFlow<List<Long>>(mutableListOf())
    val incomeExpandedCategories: StateFlow<List<Long>> = _incomeExpandedCategories.asStateFlow()

    private fun toggleCategory(categoryId:Long){
        if(_expandedCategories.value.contains(categoryId)){
           _expandedCategories.value =  _expandedCategories.value.filter { it != categoryId }
        }else{
            _expandedCategories.value += categoryId
        }
    }
    private fun toggleIncomeCategory(categoryId:Long){
        if(_incomeExpandedCategories.value.contains(categoryId)){
            _incomeExpandedCategories.value =  _incomeExpandedCategories.value.filter { it != categoryId }
        }else{
            _incomeExpandedCategories.value += categoryId
        }
    }
    private fun expandCategories(list:List<Long>){
        _expandedCategories.value = list
    }
    private fun expandIncomeCategories(list:List<Long>){
        _incomeExpandedCategories.value = list
    }
    private fun setTempParentCategory(id:Long?= null, name:String = "None",) {
        _tempParentCategory.value = Pair( id,name)
    }
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    fun getSelectedDuration(): String {
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
    private fun getSelectedAccountId(): Long {
        return runBlocking {
            preferenceUseCases.getPreferenceValue.invoke(PreferenceKeys.selectedAccountId,1L).first()
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
    init {
        onEvent(CategoryEvent.UpdateAccount(getSelectedAccountId()))
        val duration  = TransactionDuration.entries.find { it.name == getSelectedDuration() }
        duration?.let {
            val (from, to) = getFromTo(duration)
            onEvent(CategoryEvent.SetTimeStamp(from,to))
        }
        onEvent(CategoryEvent.LoadCategories)
        onEvent(CategoryEvent.LoadPieChart)
        onEvent(CategoryEvent.LoadSubCategoryPieChartDetail)
    }
    private fun updateAccountId(accountId:Long){
        _state.update {
            it.copy(
                accountId = accountId
            )
        }
    }
    private fun setTimeStamp(fromTimeStamp: Long, toTimeStamp: Long){
        _state.update {
            it.copy(
                fromTimeStamp = fromTimeStamp,
                toTimeStamp = toTimeStamp
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCategoriesWithSubcategories() {
        viewModelScope.launch {
            val categoriesFlow = categoryUseCases.getAllCategories()
            val subcategoriesFlow = categoriesFlow.flatMapLatest { cats ->
                val subcategoryFlows = cats.map { cat -> subCategoryUseCases.getSubCategoriesByCategoryId(cat.categoryId) }
                combine(subcategoryFlows) { it.toList().flatten() }
            }

            val transactionsByCategoryFlow = _state
                .map { it.accountId ?: 1L }
                .distinctUntilChanged()
                .flatMapLatest { accountId ->
                    categoryUseCases.getTransactionByCategory(accountId)
                }

            val transactionsBySubcategoryFlow = _state
                .map { it.accountId ?: 1L }
                .distinctUntilChanged()
                .flatMapLatest { accountId ->
                    subCategoryUseCases.getTransactionBySubCategory(accountId)
                }

            val transactionsFlow = combine(
                _state.map { it.fromTimeStamp },
                _state.map { it.toTimeStamp },
                transactionsByCategoryFlow,
                transactionsBySubcategoryFlow
            ) { fromTs, toTs, categoryTransactions, subcategoryTransactions ->

                val (resolvedFrom, resolvedTo) = if (fromTs != null && toTs != null) {
                    fromTs to toTs
                } else {
                    val duration = TransactionDuration.entries.find { it.name == getSelectedDuration() }
                    getFromTo(duration?:TransactionDuration.OneMonth)
                }

                // Filter all transactions up front
                val filteredCatMap = categoryTransactions.mapValues { (id, txs) -> txs.filter { it.date in resolvedFrom..resolvedTo } }
                val filteredSubCatMap = subcategoryTransactions.mapValues { (id, txs) -> txs.filter { it.date in resolvedFrom..resolvedTo } }

                filteredCatMap to filteredSubCatMap
            }

            // Finally, merge stable lists + transaction data
            combine(
                categoriesFlow,
                subcategoriesFlow,
                transactionsFlow
            ) { allCategories, allSubcategories, (filteredCatMap, filteredSubCatMap) ->

                val updatedCategories = allCategories.map { catModel ->
                    val txs = filteredCatMap[catModel.categoryId].orEmpty()
                    catModel.copy(
                        transactionTotal = txs.sumOf { it.amount.toSafeDouble() }.toString(),
                        currency = txs.firstOrNull()?.currency.orEmpty()
                    )
                }

                val updatedSubcategories = allSubcategories.map { subCatModel ->
                    val txs = filteredSubCatMap[subCatModel.subCategoryId].orEmpty()
                    subCatModel.copy(
                        transactionTotal = txs.sumOf { it.amount.toSafeDouble() }.toString(),
                        currency = txs.firstOrNull()?.currency.orEmpty()
                    )
                }

                updatedCategories to updatedSubcategories
            }
                .collectLatest { (updatedCategories, updatedSubcategories) ->
                    _state.update { oldState ->
                        oldState.copy(
                            expenseCategories = updatedCategories.filter { it.categoryType == CategoryType.Expense.name },
                            incomeCategories = updatedCategories.filter { it.categoryType == CategoryType.Income.name },
                            subCategoriesMap = updatedSubcategories.groupBy { it.categoryId },
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
    fun onEvent(event: UiEvent){
        when(event){
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
    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.SetCategoryName -> {
                _insertState.update { it.copy(categoryName = event.name) }
            }

            is CategoryEvent.SetParentCategory -> {
                _insertState.update {
                    it.copy(
                        parentCategoryName = event.name,
                        selectedParentCategoryId = event.id
                    )
                }
            }

            is CategoryEvent.InsertOrUpdateCategory -> {
                executeInsertOrUpdate(isCategory = true, model = event.category)
            }

            is CategoryEvent.InsertOrUpdateSubCategory -> {
                executeInsertOrUpdate(isCategory = false, model = event.subCategory)
            }

            is CategoryEvent.DeleteByTargetId -> {
                _insertState.value.targetId?.let {
                    if (_insertState.value.selectedParentCategoryId == null){
                        deleteCategory(it)
                    }else{
                        deleteSubCategory(it)
                    }
                }
            }
            CategoryEvent.LoadCategories -> {
                observeCategoriesWithSubcategories()
            }
            CategoryEvent.LoadPieChart -> {
                observePieChartData()
            }
            is CategoryEvent.SetPieChartCategoryId -> {
                _pieChartParentCategoryId.update {
                    event.parentCategoryId
                }
            }

            is CategoryEvent.SetCategoryUrgency -> setUrgency(event.urgency)
            is CategoryEvent.ShowUrgencySelectionDialog -> showUrgencySelectionDialog(event.show)
            is CategoryEvent.ShowDeleteConfirmationDialog -> showDeleteConfirmationDialog(event.show)
            is CategoryEvent.ShowDeleteUpdateDialog -> showDeleteUpdateDialog(event.show)
            is CategoryEvent.UpdateSearchText -> onSearchTextChanged(event.text)
            is CategoryEvent.InsertUpdateMode -> setInsertUpdateMode(event.isUpdate)
            is CategoryEvent.UpdateTargetId -> updateTargetId(event.targetId,
                event.categoryName, event.parentCategoryName, event.selectedParentCategoryId)

            is CategoryEvent.UpdateTempParentCategory -> setTempParentCategory(event.id, event.name)
            is CategoryEvent.UpdateAccount -> updateAccountId(event.accountId)
            is CategoryEvent.SetTimeStamp -> setTimeStamp(event.fromTimeStamp, event.toTimeStamp)
            is CategoryEvent.ExpandCategories ->{
                if(event.isExpense)
                    expandCategories(event.categoryIdList)
                else    expandIncomeCategories(event.categoryIdList)
            }

            is CategoryEvent.ToggleCategory ->{
                if(event.isExpense)
                    toggleCategory(event.categoryId)
                else   toggleIncomeCategory(event.categoryId)
            }

            is CategoryEvent.LoadCategoryPieChartDetail -> {
                _isPieChartExpense.value = event.isExpense
            }

            CategoryEvent.LoadSubCategoryPieChartDetail -> {
                observeSubCategoryPieChartDetail()
            }
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSubCategoryPieChartDetail(){
        viewModelScope.launch {
            val categoriesFlow = categoryUseCases.getAllCategories()
            val transactionsByCategoryFlow = _state
                .map { it.accountId ?: 1L }
                .distinctUntilChanged()
                .flatMapLatest { accountId -> categoryUseCases.getTransactionByCategory(accountId) }
            val subcategoriesFlow = _pieChartParentCategoryId
                .flatMapLatest { parentId ->
                    parentId?.let {
                        subCategoryUseCases.getSubCategoriesByCategoryId(it)
                    } ?: flowOf(emptyList())
                }
            val transactionsBySubcategoryFlow = _state
                .map { it.accountId ?: 1L }
                .distinctUntilChanged()
                .flatMapLatest { accountId ->
                    subCategoryUseCases.getTransactionBySubCategory(
                        accountId
                    )
                }
            combine(
                _state.map { it.fromTimeStamp },
                _state.map { it.toTimeStamp },
                _pieChartParentCategoryId
            ) { fromTs, toTs, parentCatId ->
                PieChartParams2(
                    fromTimeStamp = fromTs,
                    toTimeStamp = toTs,
                    parentCategoryId = parentCatId,
                )
            }.distinctUntilChanged().flatMapLatest {  params->
                combine(
                    categoriesFlow.distinctUntilChanged(),
                    transactionsByCategoryFlow.distinctUntilChanged(),
                    subcategoriesFlow.distinctUntilChanged(),
                    transactionsBySubcategoryFlow.distinctUntilChanged()
                ) { allCategories, transactionsByCat, subCategories, transactionsBySubCat ->
                    val filteredCatMap = transactionsByCat.mapValues { (_, txs) ->
                        val (resolvedFrom, resolvedTo) = if (params.fromTimeStamp != null && params.toTimeStamp != null) {
                            params.fromTimeStamp to params.toTimeStamp
                        } else {
                            val duration = TransactionDuration.entries.find { it.name == getSelectedDuration() }
                            getFromTo(duration?:TransactionDuration.OneMonth)
                        }
                        txs.filter { it.date in resolvedFrom..resolvedTo
                        }
                    }
                    val totalByCategory = filteredCatMap.mapValues { (_, txs) -> txs.sumOf { it.amount.toSafeDouble() } }
                    val filteredSubCatMap = transactionsBySubCat.mapValues { (_, txs) ->
                        val (resolvedFrom, resolvedTo) = if (params.fromTimeStamp != null && params.toTimeStamp != null) {
                            params.fromTimeStamp to params.toTimeStamp
                        } else {
                            val duration =
                                TransactionDuration.entries.find { it.name == getSelectedDuration() }
                            getFromTo(duration?:TransactionDuration.OneMonth)
                        }
                        txs.filter { it.date in resolvedFrom..resolvedTo }
                    }
                    val title = allCategories.find { it.categoryId == params.parentCategoryId }?.categoryName?:""
                    val totalBySubCat = filteredSubCatMap.mapValues { (_, txs) -> txs.sumOf { it.amount.toSafeDouble() } }
                    val totalAmount = totalByCategory[params.parentCategoryId]?:0.0
                    title to subCategories.mapToSubcategorySlices(totalBySubCat, totalAmount)
                }
            }.onStart {
                _subcategoryPieChartState.update { it.copy(isLoading = true, error = null) }
            }.catch {e->
                _subcategoryPieChartState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }.collectLatest { (title, slices) ->
                _subcategoryPieChartState.update {
                    it.copy(
                        screenTitle = title,
                        slices = slices,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }
    private fun setUrgency(urgency: String) {
        _insertState.update { it.copy(urgency = urgency) }
    }
    private fun setInsertUpdateMode(isUpdate: Boolean) {
        _insertState.update { it.copy(isUpdateMode = isUpdate) }
    }
    private fun updateTargetId(targetId:Long?= null, categoryName: String = "", parentCategoryName: String = "None", selectedParentCategoryId: Long? = null,) {
        _insertState.update { it.copy( targetId = targetId, categoryName = categoryName, parentCategoryName = parentCategoryName, selectedParentCategoryId = selectedParentCategoryId) }
    }
    private fun showUrgencySelectionDialog(show: Boolean) {
        _insertState.update { it.copy(showUrgencySelectionDialog = show) }
    }
    private fun showDeleteUpdateDialog(show: Boolean) {
        _state.update { it.copy(showUpdateDeleteDialog = show) }
    }
    private fun showDeleteConfirmationDialog(show: Boolean) {
        _state.update { it.copy(showDeleteConfirmationDialog = show) }
    }
    private fun <T> executeInsertOrUpdate(isCategory: Boolean, model: T) {
        val insertState = insertState.value
        viewModelScope.launch {
            _insertState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                if (isCategory && model is CategoryModel) {
                    if (model.categoryName.trim()
                            .isEmpty()
                    ) throw IllegalArgumentException("Category Name can't be empty")
                    if (insertState.isUpdateMode) categoryUseCases.updateCategory(model)
                    else categoryUseCases.insertCategory(model)
                } else if (!isCategory && model is SubCategoryModel) {
                    if (model.subCategoryName.trim()
                            .isEmpty()
                    ) throw IllegalArgumentException("SubCategory Name can't be empty")
                    if (insertState.isUpdateMode) subCategoryUseCases.updateSubCategory(model)
                    else subCategoryUseCases.insertSubCategory(model)
                }
                _insertState.update { it.copy(isSuccess = true, isLoading = false) }
                _eventFlow.emit(UiEvent.Saved(
                    if(isCategory) {
                        if(insertState.isUpdateMode){
                            "Category Updated Successfully!"
                        }else{
                            "Category Saved Successfully!"
                        }
                    }
                    else{
                        if(insertState.isUpdateMode) {
                            "SubCategory Updated Successfully!"
                        }else{
                            "SubCategory Saved Successfully!"
                        }
                    }
                ))
                onEvent(CategoryEvent.UpdateTargetId())
            } catch (e: Exception) {
                _insertState.update { it.copy(isLoading = false, errorMessage = e.message) }
                _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Insert/Update failed"))
            }
        }
    }

    private fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            try {
                categoryUseCases.deleteCategoryById(categoryId)
                _eventFlow.emit(UiEvent.ShowToast("Category Deleted Successfully!"))
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Delete failed"))
            }
        }
    }
    private fun deleteSubCategory(subCategoryId: Long) {
        viewModelScope.launch {
            try {
                subCategoryUseCases.deleteSubCategoryById(subCategoryId)
                _eventFlow.emit(UiEvent.ShowToast("SubCategory Deleted Successfully!"))
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Delete failed"))
            }
        }
    }
    suspend fun getCategoryById(id: Long): CategoryModel? = try {
        categoryUseCases.getCategoryById(id)
    } catch (e: Exception) {
        _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Get failed"))
        null
    }

    suspend fun getSubCategoryById(id: Long): SubCategoryModel? = try {
        subCategoryUseCases.getSubCategoryById(id)
    } catch (e: Exception) {
        _eventFlow.emit(UiEvent.ShowToast(e.message ?: "Get failed"))
        null
    }
    data class PieChartParams(
        val allCategories: List<CategoryModel>,
        val transactionsByCat: Map<Long, List<TransactionModel>>,
        val fromTs: Long?,
        val toTs: Long?,
        val isExpense: Boolean
    )
    data class PieChartParams2(
        val fromTimeStamp: Long?,
        val toTimeStamp: Long?,
        val parentCategoryId: Long?
    )
    private fun List<CategoryModel>.mapToSlices(
        totalByCategory: Map<Long, Double>,
        grandTotal: Double
    ): List<PieSlice> {
        return map { cat ->
            val sum = totalByCategory[cat.categoryId] ?: 0.0
            PieSlice(
                categoryId = cat.categoryId,
                label = cat.categoryName,
                amount = sum,
                proportion = if (grandTotal == 0.0) 0f else (sum / grandTotal).toFloat(),
                color = Utils.lerpColor(
                    Utils.themeColors.random(),
                    Utils.themeColors.random(),
                    Random.nextFloat()
                )
            )
        }
    }
    fun getFromTo(duration: TransactionDuration):Pair<Long,Long>{
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
    private fun List<SubCategoryModel>.mapToSubcategorySlices(
        totalBySubCategory: Map<Long, Double>,
        grandTotal: Double
    ): List<PieSlice> {
        return map { subCat ->
            val sum = totalBySubCategory[subCat.subCategoryId] ?: 0.0
            PieSlice(
                categoryId = subCat.subCategoryId,
                label = subCat.subCategoryName,
                amount = sum,
                proportion = if (grandTotal == 0.0) 0f else (sum / grandTotal).toFloat(),
                color = Utils.lerpColor(
                    Utils.themeColors.random(),
                    Utils.themeColors.random(),
                    Random.nextFloat()
                )
            )
        }
    }
}
