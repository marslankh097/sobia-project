package com.example.budgetly.ui.cash.category.screens.category_display
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.domain.models.enums.common.Options
import com.example.budgetly.domain.models.enums.transaction.TransactionScreenSource
import com.example.budgetly.ui.cash.accounts.AccountsViewModel
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.ui.cash.category.screens.category_display.content.CategoryDisplayPage
import com.example.budgetly.ui.cash.filter.events.FilterEvent
import com.example.budgetly.ui.cash.transaction.TransactionsViewModel
import com.example.budgetly.ui.cash.transaction.events.TransactionEvent
import com.example.budgetly.ui.cash.transaction.events.TransactionInsertEvent
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.bgShade1
import com.example.budgetly.utils.dialog.SimpleAlertDialog
import com.example.budgetly.utils.dialog.UpdateDeleteDialog
import com.example.budgetly.utils.inactiveTextColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.FailureComponent
import com.example.budgetly.utils.shared_components.ImageSwitchRow
import com.example.budgetly.utils.shared_components.LoadingComponent
import com.example.budgetly.utils.shared_components.SearchBar
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toast
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun CategoryDisplayScreen(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    navigateBack: () -> Unit,
    navigateToCategoryInsertUpdate: (Boolean) -> Unit,
    navigateToTransactionDisplay: () -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    val categoryState by categoryViewModel.state.collectAsState()
    val accountState by accountsViewModel.accountState.collectAsState()
    val insertState by categoryViewModel.insertState.collectAsState()
    val expenseExpandedCategoryIds by categoryViewModel.expandedCategories.collectAsState()
    val incomeExpandedCategoryIds by categoryViewModel.incomeExpandedCategories.collectAsState()
    val searchText by categoryViewModel.searchText.collectAsState()
    val initialPage = if (isExpense) 0 else 1
    val pagerState = rememberPagerState(initialPage = initialPage)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val handleBack = { if (pagerState.currentPage == 0) navigateBack()
    else coroutineScope.launch { pagerState.scrollToPage(0) }}
    var currentIsExpense by remember {
        mutableStateOf(isExpense)
    }
    // Handle events like toast
    LaunchedEffect(Unit) {
        categoryViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> context.toast(event.message)
                is UiEvent.Saved -> context.toast(event.message)
            }
        }
    }

    BackHandler {
        handleBack()
    }
    // ⚠️ Show global dialogs
    if (categoryState.showDeleteConfirmationDialog) {
        SimpleAlertDialog(
            modifier = Modifier.fillMaxWidth(),
            title = if (insertState.selectedParentCategoryId == null)
                stringResource(R.string.delete_this_category)
            else stringResource(R.string.delete_this_subcategory),
            msg = if (insertState.selectedParentCategoryId == null)
                stringResource(R.string.are_you_sure_you_want_to_delete_this_category)
            else stringResource(R.string.are_you_sure_you_want_to_delete_this_subcategory),
            positiveText = stringResource(R.string.delete),
            negativeText = stringResource(R.string.cancel),
            onButtonClick = {
                categoryViewModel.onEvent(CategoryEvent.ShowDeleteConfirmationDialog(false))
                if (it) categoryViewModel.onEvent(CategoryEvent.DeleteByTargetId)
            }
        )
    }

    if (categoryState.showUpdateDeleteDialog) {
        UpdateDeleteDialog(
            title = if (insertState.selectedParentCategoryId == null)
                stringResource(R.string.category)
            else stringResource(R.string.subcategory),
            onOptionSelected = { option ->
                categoryViewModel.onEvent(CategoryEvent.ShowDeleteUpdateDialog(false))
                if (option == Options.Delete.name) {
                    categoryViewModel.onEvent(CategoryEvent.ShowDeleteConfirmationDialog(true))
                } else if (option == Options.Update.name) {
                    categoryViewModel.onEvent(CategoryEvent.InsertUpdateMode(true))
                    navigateToCategoryInsertUpdate(currentIsExpense)
                }
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(if (currentIsExpense) R.string.expense_categories else R.string.income_categories),
            onClick = { if (pagerState.currentPage == 0) handleBack() else coroutineScope.launch { pagerState.scrollToPage(0) } }
        )
        // 🔁 Main Content Handling
        when {
            categoryState.isLoading -> {
                LoadingComponent(
                    textId = if (pagerState.currentPage == 1)
                        R.string.loading_income_categories
                    else R.string.loading_expense_categories
                )
            }

            categoryState.error != null -> {
                FailureComponent(
                    msg = categoryState.error!!,
                    onTryAgainClick = {
                        categoryViewModel.onEvent(CategoryEvent.LoadCategories)
                    }
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(secondaryBgColor)
                ) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        VerticalSpacer()
                        SearchBar(
                            text = searchText,
                            hint = "Search a Category Name here",
                            backgroundColor = bgShade1,
                            borderColor = inactiveTextColor,
                            iconCrossClick = {
                                categoryViewModel.onEvent(
                                    CategoryEvent.UpdateSearchText(
                                        ""
                                    )
                                )
                            },
                            onValueChange = {
                                categoryViewModel.onEvent(
                                    CategoryEvent.UpdateSearchText(
                                        it
                                    )
                                )
                            }
                        )
                        VerticalSpacer()
                        ImageSwitchRow(
                            imageId1 = R.drawable.icon_expense,
                            imageId2 = R.drawable.icon_income,
                            tabText1 = stringResource(R.string.expense),
                            tabText2 = stringResource(R.string.income),
                            switchOn = pagerState.currentPage == 1,
                            switchChanged = { isOn ->
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(if (isOn) 1 else 0) }
                            }
                        )
                        VerticalSpacer()
                        HorizontalPager(
                            count = 2,
                            state = pagerState,
                            modifier = Modifier.weight(1f)
                        ) { page ->
                            currentIsExpense = pagerState.currentPage == 0
                            CategoryDisplayPage(
                                modifier = Modifier.weight(1f),
                                isExpense = currentIsExpense,
                                categoryState = categoryState,
                                searchText = searchText,
                                expenseExpandedCategoryIds = expenseExpandedCategoryIds,
                                incomeExpandedCategoryIds = incomeExpandedCategoryIds,
                                onCategoryExpanded = { expense, id ->
                                    categoryViewModel.onEvent(CategoryEvent.ToggleCategory(expense,id))
                                },
                                expandCategories = { expense, list->
                                    categoryViewModel.onEvent(CategoryEvent.ExpandCategories(expense,list))
                                },
                                onLongPressCategory = { category ->
                                    categoryViewModel.onEvent(CategoryEvent.UpdateTargetId(category.categoryId, category.categoryName))
                                    categoryViewModel.onEvent(CategoryEvent.ShowDeleteUpdateDialog(true))
                                },
                                onLongPressSubCategory = { subCategory, category ->
                                    categoryViewModel.onEvent(
                                        CategoryEvent.UpdateTargetId(
                                            subCategory.subCategoryId,
                                            subCategory.subCategoryName,
                                            category.categoryName,
                                            category.categoryId
                                        )
                                    )
                                    categoryViewModel.onEvent(CategoryEvent.ShowDeleteUpdateDialog(true))
                                },
                                onCategorySelected = { category, subCategory ->
                                    transactionViewModel.onEvent(TransactionEvent.SetDisplayCategorySubCategoryAccount(category, subCategory,accountState.selectedAccount))
                                    transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionType(if (currentIsExpense) "Expense" else "Income"))
                                    transactionViewModel.onEvent(FilterEvent.SetScreenSource(TransactionScreenSource.Category.name))
                                    navigateToTransactionDisplay()
                                },
                                onSubCategorySelected = { category, subCategory ->
                                    transactionViewModel.onEvent(TransactionInsertEvent.SetTransactionType(if (currentIsExpense) "Expense" else "Income"))
                                    transactionViewModel.onEvent(TransactionEvent.SetDisplayCategorySubCategoryAccount(category, subCategory,accountState.selectedAccount))
                                    transactionViewModel.onEvent(FilterEvent.SetScreenSource(TransactionScreenSource.SubCategory.name))
                                    navigateToTransactionDisplay()
                                }
                            )
                        }
                    }
                    Image(
                        modifier = Modifier
                            .size(70.sdp)
                            .align(Alignment.BottomEnd)
                            .padding(end = 30.sdp, bottom = 30.sdp)
                            .safeClickAble {
                                categoryViewModel.onEvent(CategoryEvent.InsertUpdateMode(false))
                                categoryViewModel.onEvent(CategoryEvent.UpdateTargetId())
                                navigateToCategoryInsertUpdate(currentIsExpense)
                            },
                        painter = painterResource(R.drawable.icon_add_gradient),
                        contentDescription = if (currentIsExpense) "Add New Expense Category" else "Add New Income Category"
                    )
                }
            }
        }
    }
}

/*@Composable
fun CategoryDisplayScreen(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    navigateBack: () -> Unit,
    navigateToCategoryInsertUpdate: () -> Unit,
    navigateToTransactionDisplay:()->Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    transactionViewModel:TransactionsViewModel = hiltViewModel()
) {
    val categoryState by categoryViewModel.state.collectAsState()
    val insertState by categoryViewModel.insertState.collectAsState()
    val expandedCategoryIds  by categoryViewModel.expandedCategories.collectAsState()
    val searchText by categoryViewModel.searchText.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        categoryViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    context.toast(event.message)
                }
                is UiEvent.Saved -> {
                    context.toast(event.message)
                }
            }
        }
    }
    val categories = if (isExpense) categoryState.expenseCategories else categoryState.incomeCategories

    // Filtered categories and expanded state
    val filteredCategories = remember(searchText, categoryState.isLoading, categoryState.error, categoryState.expenseCategories,
        categoryState.incomeCategories, categoryState.subCategoriesMap) {
        val trimmedSearch = searchText.trim()
        val matchingIds = expandedCategoryIds.toMutableList()
        val result = if (!categoryState.isLoading && categoryState.error == null) {
            if (trimmedSearch.isNotEmpty()) {
                matchingIds.clear()
                val mappedCategories = categories.mapNotNull { category ->
                    val subCategories = categoryState.subCategoriesMap[category.categoryId].orEmpty()
                    val categoryMatches = category.categoryName.contains(trimmedSearch, ignoreCase = true)
                    val matchingSubCategories = subCategories.filter {
                        it.subCategoryName.contains(trimmedSearch, ignoreCase = true)
                    }
                    when {
                        categoryMatches -> {
                            matchingIds.add(category.categoryId)
                            category
                        }

                        matchingSubCategories.isNotEmpty() -> {
                            matchingIds.add(category.categoryId)
                            category
                        }

                        else -> null
                    }
                }
                mappedCategories
            } else{
                categories
            }
        } else emptyList()
        categoryViewModel.onEvent(CategoryEvent.ExpandCategories(matchingIds))
        result
    }

    val handleBack = {navigateBack()}
    BackHandler { handleBack() }

    if(categoryState.showDeleteConfirmationDialog){
        SimpleAlertDialog(
            modifier = Modifier.fillMaxWidth(),
            title =
                if(insertState.selectedParentCategoryId == null)
                    stringResource(R.string.delete_this_category) else
                    stringResource(R.string.delete_this_subcategory)
            ,
            msg = if(insertState.selectedParentCategoryId == null)
                stringResource(R.string.are_you_sure_you_want_to_delete_this_category) else
                    stringResource(R.string.are_you_sure_you_want_to_delete_this_subcategory)
            ,
            positiveText = stringResource(R.string.delete),
            negativeText = stringResource(R.string.cancel),
            onButtonClick = {
                categoryViewModel.onEvent(CategoryEvent.ShowDeleteConfirmationDialog(false))
                if(it){
                    categoryViewModel.onEvent(CategoryEvent.DeleteByTargetId)
                }
            }
        )
    }
    if(categoryState.showUpdateDeleteDialog){
        UpdateDeleteDialog(title =
            if(insertState.selectedParentCategoryId == null) stringResource(R.string.category) else stringResource(R.string.subcategory),
            onOptionSelected = { selectedOption ->
            categoryViewModel.onEvent(CategoryEvent.ShowDeleteUpdateDialog(false))
            if(selectedOption.isNotEmpty()){
                if(selectedOption == Options.Delete.name){
                    categoryViewModel.onEvent(CategoryEvent.ShowDeleteConfirmationDialog(true))
                }else{
                    categoryViewModel.onEvent(CategoryEvent.InsertUpdateMode(true))
                    navigateToCategoryInsertUpdate()
                }
            }
        })
    }
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(if (isExpense) R.string.expense_categories else R.string.income_categories),
            onClick = handleBack
        )

        when {
            categoryState.isLoading -> {
                LoadingComponent(
                    textId = if (isExpense) R.string.loading_expense_categories else R.string.loading_income_categories
                )
            }

            categoryState.error != null -> {
                FailureComponent(
                    msg = categoryState.error!!,
                    onTryAgainClick = { categoryViewModel.onEvent(CategoryEvent.LoadCategories) }
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(secondaryBgColor)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        VerticalSpacer()
                        SearchBar(
                            text = searchText,
                            hint = "Search a Category Name here",
                            backgroundColor = bgShade1,
                            borderColor = inactiveTextColor,
                            iconCrossClick = {categoryViewModel.onEvent(CategoryEvent.UpdateSearchText("") )},
                            onValueChange = { categoryViewModel.onEvent(CategoryEvent.UpdateSearchText(it))}
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 12.sdp, vertical = 12.sdp),
                            contentPadding = PaddingValues(bottom = 60.sdp),
                            verticalArrangement = Arrangement.spacedBy(10.sdp)
                        ) {
                            items(filteredCategories) { category ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    CategoryRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        title = category.categoryName,
                                        subTitle = category.transactionTotal,
                                        currency = category.currency,
                                        iconId = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                                        isExpanded = category.categoryId in expandedCategoryIds,
                                        searchText = searchText,
                                        onCategorySelected = {
                                            val firstSubCategory = categoryState.subCategoriesMap[category.categoryId]?.firstOrNull()
                                            transactionViewModel.onEvent(TransactionEvent.SetCategoryAndSubCategory(category,
                                                firstSubCategory, false, true))
                                            transactionViewModel.onEvent(TransactionEvent.SetTransactionType(
                                                if(isExpense) TransactionType.Expense.name else TransactionType.Income.name
                                            ))
                                            transactionViewModel.onEvent(TransactionEvent.SetScreenSource(TransactionScreenSource.Category.name))
                                            navigateToTransactionDisplay()
                                        },
                                        onCategoryExpanded = {
                                            log("expandedCategoryIds:$expandedCategoryIds..... category.categoryId: ${category.categoryId}")
                                            categoryViewModel.onEvent(CategoryEvent.ToggleCategory(category.categoryId))
                                        },
                                        onLongPressed = {
                                            log("onLongPressed:${category.categoryId}")
                                            categoryViewModel.onEvent(CategoryEvent.UpdateTargetId(category.categoryId, category.categoryName))
                                            categoryViewModel.onEvent(CategoryEvent.ShowDeleteUpdateDialog(true))
                                        }
                                    )

                                    if (category.categoryId in expandedCategoryIds) {
                                        VerticalSpacer()
                                        val allSubCategories = categoryState.subCategoriesMap[category.categoryId].orEmpty()
                                        val visibleSubCategories = if (searchText.isNotBlank()) {
                                            allSubCategories.filter {
                                                it.subCategoryName.contains(searchText.trim(), ignoreCase = true)
                                            }
                                        } else allSubCategories

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 10.sdp),
                                            verticalArrangement = Arrangement.spacedBy(10.sdp)
                                        ) {
                                            visibleSubCategories.forEach { subCategory ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    ThreeDots()
                                                    SubCategoryRow(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        iconId = if (isExpense) R.drawable.img_category_expense else R.drawable.img_category_income,
                                                        title = subCategory.subCategoryName,
                                                        subTitle = subCategory.transactionTotal,
                                                        currency = subCategory.currency,
                                                        searchText = searchText,
                                                        onSubCategorySelected = {
                                                            // Handle subcategory selection if needed
                                                            transactionViewModel.onEvent(TransactionEvent.SetTransactionType(
                                                                if(isExpense) TransactionType.Expense.name else TransactionType.Income.name
                                                            ))
                                                            transactionViewModel.onEvent(TransactionEvent.SetCategoryAndSubCategory(category,
                                                                subCategory, false, false))
                                                            transactionViewModel.onEvent(TransactionEvent.SetScreenSource(TransactionScreenSource.SubCategory.name))
                                                            navigateToTransactionDisplay()
                                                        },
                                                        onLongPressed = {
                                                            categoryViewModel.onEvent(CategoryEvent.UpdateTargetId(subCategory.subCategoryId,
                                                                subCategory.subCategoryName, category.categoryName, category.categoryId))
                                                            categoryViewModel.onEvent(CategoryEvent.ShowDeleteUpdateDialog(true))
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Image(
                        modifier = Modifier
                            .size(70.sdp)
                            .align(Alignment.BottomEnd)
                            .padding(end = 30.sdp, bottom = 30.sdp)
                            .safeClickAble {
                                categoryViewModel.onEvent(CategoryEvent.InsertUpdateMode(false))
                                categoryViewModel.onEvent(CategoryEvent.UpdateTargetId())
                                navigateToCategoryInsertUpdate()
                            },
                        painter = painterResource(R.drawable.icon_add_gradient),
                        contentDescription = if (isExpense) "Add New Expense Category" else "Add New Income Category"
                    )
                }
            }
        }
    }
}*/
