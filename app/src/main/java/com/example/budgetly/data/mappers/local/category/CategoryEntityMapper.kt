package com.example.budgetly.data.mappers.local.category
import com.example.budgetly.data.local.database.entities.local.cash.CategoryEntity
import com.example.budgetly.domain.models.db.transactions.CategoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun CategoryEntity.toCategoryModel(): CategoryModel {
    return CategoryModel(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        categoryType = this.categoryType,
        predefined = predefined
    )
}

fun CategoryModel.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        categoryType = this.categoryType,
        predefined = predefined
    )
}

fun List<CategoryEntity>.toCategoryModelList(): List<CategoryModel> =
    this.map { it.toCategoryModel() }

fun List<CategoryModel>.toCategoryEntityList(): List<CategoryEntity> =
    this.map { it.toCategoryEntity() }

fun Flow<List<CategoryEntity>>.toCategoryModelFlow(): Flow<List<CategoryModel>> =
    this.map { it.toCategoryModelList() }

fun Flow<List<CategoryModel>>.toCategoryEntityFlow(): Flow<List<CategoryEntity>> =
    this.map { it.toCategoryEntityList() }
