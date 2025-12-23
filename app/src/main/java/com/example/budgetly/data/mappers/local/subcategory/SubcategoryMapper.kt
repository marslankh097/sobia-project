package com.example.budgetly.data.mappers.local.subcategory
import com.example.budgetly.data.local.database.entities.local.cash.SubCategoryEntity
import com.example.budgetly.domain.models.db.transactions.SubCategoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun SubCategoryEntity.toSubCategoryModel(): SubCategoryModel {
    return SubCategoryModel(
        categoryId = this.categoryId,
        subCategoryId = this.subCategoryId,
        subCategoryName = this.subCategoryName,
        urgency = this.urgency,
        predefined = predefined
    )
}

fun SubCategoryModel.toSubCategoryEntity(): SubCategoryEntity {
    return SubCategoryEntity(
        categoryId = this.categoryId,
        subCategoryId = this.subCategoryId,
        subCategoryName = this.subCategoryName,
        urgency = this.urgency,
        predefined = predefined
    )
}

fun List<SubCategoryEntity>.toSubCategoryModelList(): List<SubCategoryModel> =
    this.map { it.toSubCategoryModel() }

fun List<SubCategoryModel>.toSubCategoryEntityList(): List<SubCategoryEntity> =
    this.map { it.toSubCategoryEntity() }

fun Flow<List<SubCategoryEntity>>.toSubCategoryModelFlow(): Flow<List<SubCategoryModel>> =
    this.map { it.toSubCategoryModelList() }

fun Flow<List<SubCategoryModel>>.toSubCategoryEntityFlow(): Flow<List<SubCategoryEntity>> =
    this.map { it.toSubCategoryEntityList() }
