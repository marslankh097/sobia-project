package com.example.budgetly.ui.cash.category.screens.category_insert.content

import android.app.AlertDialog
import android.content.Context
import com.example.budgetly.domain.models.enums.category.CategoryUrgency

fun showUrgencySelectionDialog(context: Context, onSelect: (String) -> Unit) {
    val options = CategoryUrgency.entries.map { it.name }
    AlertDialog.Builder(context)
        .setTitle("Select Urgency")
        .setItems(options.toTypedArray()) { _, index ->
            onSelect(options[index])
        }
        .show()
}
