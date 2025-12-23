package com.example.budgetly.utils.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import com.demo.budgetly.R
import com.example.budgetly.utils.ButtonMedium
import com.example.budgetly.utils.LabelLarge
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.grey
import com.example.budgetly.utils.inactiveButtonColor
import com.example.budgetly.utils.inactiveTextColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.PrimaryButtonRounded
import com.example.budgetly.utils.shared_components.RoundedBorderRectangle
import com.example.budgetly.utils.shared_components.SecondaryButtonRounded
import com.example.budgetly.utils.white
import ir.kaaveh.sdpcompose.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    modifier: Modifier =Modifier,
    initialDateMillis: Long = System.currentTimeMillis(),
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    BaseDialog(
        onButtonClick = { confirmed ->
            if (confirmed) {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
            } else {
                onDismiss()
            }
        },
        dismissOnClickOutside = true,
        dismissOnBackPress = true
    ) {
        RoundedBorderRectangle(
            modifier = modifier.padding(12.sdp),
            bgColor = itemBgColor,
            borderColor = Color.Transparent,
            borderRadius = 8f, borderThickness = 1f
        ){
            Column(modifier = modifier.padding(15.sdp), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "Select Date", style = SubtitleLarge, color = primaryColor)
                VerticalSpacer(12)
                DatePicker(state = datePickerState)
                VerticalSpacer(16)
                Row(modifier = Modifier.fillMaxWidth()) {
                    SecondaryButtonRounded(modifier = Modifier
                        .weight(1f),
                        horizontalPadding = 10,
                        transparentColors = listOf(inactiveButtonColor, inactiveButtonColor),
                        gradientColors = listOf(inactiveTextColor, inactiveTextColor),
                        onButtonClick = { onDismiss.invoke() }) {
                        Text(
                            modifier = Modifier.scrollable(
                                state = rememberScrollState(),
                                orientation = Orientation.Horizontal,
                            ), text = stringResource(R.string.cancel), color = grey, style = ButtonMedium,
                        )
                    }
                    PrimaryButtonRounded(modifier = Modifier
                        .weight(1f),
                        horizontalPadding = 10,
                        onButtonClick = { datePickerState.selectedDateMillis?.let { onDateSelected(it) } }) {
                        Text(text = stringResource(R.string.apply), color = white, style = LabelLarge)
                    }
                }

            }
        }
    }
}
