package com.example.budgetly.ui.cash.category.screens.category_insert
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.demo.budgetly.R
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import com.example.budgetly.domain.models.enums.category.CategoryType
import com.example.budgetly.domain.models.enums.category.CategoryUrgency
import com.example.budgetly.ui.cash.category.CategoryViewModel
import com.example.budgetly.ui.cash.category.events.CategoryEvent
import com.example.budgetly.ui.cash.category.events.UiEvent
import com.example.budgetly.ui.cash.category.screens.category_insert.content.CustomSelectionRow
import com.example.budgetly.ui.cash.category.screens.category_insert.content.OptionSelectionDialog
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.secondaryColor
import com.example.budgetly.utils.shared_components.CustomTextField
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.toast
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CategoryInsertUpdateScreen(
    modifier: Modifier = Modifier,
    isExpense: Boolean,
    navigateBack: () -> Unit,
    navigateToParentCategorySelection: () -> Unit,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
) {
    val insertState by categoryViewModel.insertState.collectAsState()
    val context = LocalContext.current
    val handleBack = {navigateBack()}
    BackHandler { handleBack() }
    // Listen for success event
    LaunchedEffect(Unit) {
        categoryViewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.Saved -> {
                    handleBack()
                }
                is UiEvent.ShowToast -> {
                    context.toast(event.message)
                }
                else -> Unit
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopBar(stringResource(if(isExpense){
            if(insertState.selectedParentCategoryId == null){
                if(insertState.isUpdateMode)
                    R.string.update_expense_category
                else R.string.add_expense_category
            }else{
                if(insertState.isUpdateMode)
                    R.string.update_expense_subcategory
                else R.string.add_expense_subcategory
            }

        }else{
            if(insertState.selectedParentCategoryId == null) {
                if (insertState.isUpdateMode)
                    R.string.update_income_category
                else R.string.add_income_category
            }else{
                if (insertState.isUpdateMode)
                    R.string.update_income_subcategory
                else R.string.add_income_subcategory
            }
        }
        )){
            navigateBack()
        }
        if(insertState.showUrgencySelectionDialog){
            OptionSelectionDialog(modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.select_subcategory_urgency),
                options = CategoryUrgency.entries.map { it.name },
                selectedOption = insertState.urgency, onOptionSelected = {selectedUrgency->
                categoryViewModel.onEvent(CategoryEvent.ShowUrgencySelectionDialog(false))
                if(selectedUrgency.isNotEmpty()){
                    categoryViewModel.onEvent(CategoryEvent.SetCategoryUrgency(selectedUrgency))
                }
            })
        }
        Column(
            Modifier
                .fillMaxWidth()
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            Text( if (insertState.selectedParentCategoryId == null)
                stringResource(R.string.enter_category_name)
            else
                stringResource(R.string.enter_subcategory_name), style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                text = insertState.categoryName,
                hint = if (insertState.selectedParentCategoryId == null) stringResource(R.string.enter_category_name_here)
                else stringResource(R.string.enter_subcategory_name_here),
                onValueChange = { categoryViewModel.onEvent(CategoryEvent.SetCategoryName(it)) },
                iconCrossClick = { categoryViewModel.onEvent(CategoryEvent.SetCategoryName("")) }
            )
            if(insertState.isUpdateMode.not() || insertState.selectedParentCategoryId != null){
                VerticalSpacer(15)
                Text(
                    stringResource(R.string.select_parent_category),
                    style = SubtitleMedium, color = primaryColor)
                VerticalSpacer(12)
                CustomSelectionRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = insertState.parentCategoryName,
                    isExpanded = false,
                    onCategorySelected = {
                        categoryViewModel.onEvent(CategoryEvent.UpdateTempParentCategory(name = insertState.parentCategoryName, id = insertState.selectedParentCategoryId))
                        navigateToParentCategorySelection()
                    }
                )
            }
            if (insertState.selectedParentCategoryId != null && isExpense ) {
                VerticalSpacer(15)
                Text("Select SubCategory Type:", style = SubtitleMedium, color = primaryColor)
                VerticalSpacer(12)
                CustomSelectionRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = insertState.urgency,
                    isExpanded = false,
                    onCategorySelected = {
                        categoryViewModel.onEvent(CategoryEvent.ShowUrgencySelectionDialog(true))
                    }
                )
            }
            VerticalSpacer(20)
            PrimaryButtonRounded(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(secondaryColor, secondaryColor),
                isRound = false,
                onButtonClick = {
                    if (insertState.selectedParentCategoryId != null) {
                        // Means user selected a parent → create SubCategory
                        val subCategory = SubCategoryModel(
                            subCategoryId = insertState.targetId ?: 0L,
                            categoryId = insertState.selectedParentCategoryId!!,
                            subCategoryName = insertState.categoryName,
                            urgency = insertState.urgency
                        )
                        categoryViewModel.onEvent(CategoryEvent.InsertOrUpdateSubCategory(subCategory))
                    } else {
                        val category = CategoryModel(
                            categoryId = insertState.targetId ?: 0L,
                            categoryName = insertState.categoryName,
                            categoryType = if (isExpense) CategoryType.Expense.name else CategoryType.Income.name
                        )
                        categoryViewModel.onEvent(CategoryEvent.InsertOrUpdateCategory(category))
                    }
                }
            ) {
                Text(if (insertState.isUpdateMode) "Update" else "Add", style = SubtitleLarge, color = white)
            }
        }
    }
}
