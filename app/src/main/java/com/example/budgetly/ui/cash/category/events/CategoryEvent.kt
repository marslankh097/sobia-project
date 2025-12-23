package com.example.budgetly.ui.cash.category.events
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel

sealed class CategoryEvent {
    data object LoadCategories : CategoryEvent()
    data object LoadPieChart : CategoryEvent()
    data class SetPieChartCategoryId(val parentCategoryId: Long?) : CategoryEvent()
    data class LoadCategoryPieChartDetail(val isExpense:Boolean) : CategoryEvent()
    data object LoadSubCategoryPieChartDetail : CategoryEvent()
//    data class GoToSubCategoryPieChart(val parentCategoryId: Long) : CategoryEvent()
//    data object BackToMainPieChart : CategoryEvent()
    data class UpdateAccount(val accountId:Long): CategoryEvent()
    data class SetTimeStamp(val fromTimeStamp:Long, val toTimeStamp:Long) : CategoryEvent()
    data class ToggleCategory(val isExpense:Boolean,val categoryId:Long): CategoryEvent()
    data class ExpandCategories(val isExpense:Boolean, val categoryIdList:List<Long>): CategoryEvent()
    data class InsertOrUpdateCategory(val category: CategoryModel) : CategoryEvent()
    data class InsertOrUpdateSubCategory(val subCategory: SubCategoryModel) : CategoryEvent()
    data object DeleteByTargetId : CategoryEvent()
    data class SetCategoryName(val name: String) : CategoryEvent()
    data class SetCategoryUrgency(val urgency: String) : CategoryEvent()
    data class InsertUpdateMode(val isUpdate:Boolean) : CategoryEvent()
    data class UpdateTargetId(val targetId:Long? = null, val categoryName: String = "", val parentCategoryName: String = "None", val selectedParentCategoryId: Long? = null) : CategoryEvent()
    data class UpdateSearchText(val text: String) : CategoryEvent()
    data class ShowUrgencySelectionDialog(val show: Boolean) : CategoryEvent()
    data class ShowDeleteUpdateDialog(val show: Boolean) : CategoryEvent()
    data class ShowDeleteConfirmationDialog(val show: Boolean) : CategoryEvent()
    data class SetParentCategory(val name: String, val id: Long?) : CategoryEvent()
    data class UpdateTempParentCategory(val name: String, val id: Long?) : CategoryEvent()
}
