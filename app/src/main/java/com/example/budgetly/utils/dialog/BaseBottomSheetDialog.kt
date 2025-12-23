package com.example.budgetly.utils.dialog

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.example.budgetly.utils.itemBgColor
import ir.kaaveh.sdpcompose.sdp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheetDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = itemBgColor,
        shape = RoundedCornerShape(topStart = 12.sdp, topEnd = 16.sdp),
        dragHandle = null
    ) {
        content()
    }
}
