package com.example.budgetly.ui.cash.transaction.screens.date_time_picker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.budgetly.utils.SubtitleMedium
import com.example.budgetly.utils.VerticalSpacer
import com.example.budgetly.utils.log
import com.example.budgetly.utils.primaryColor
import com.example.budgetly.utils.secondaryBgColor
import com.example.budgetly.utils.shared_components.TopBar
import com.example.budgetly.utils.toDateString
import ir.kaaveh.sdpcompose.sdp
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun DateTimePickerScreen(
    modifier: Modifier = Modifier,
    initialDateTime: Long = System.currentTimeMillis(),
    onDateTimeSelected: (Long) -> Unit,
    navigateBack: () -> Unit
) {
    val handleBack = { navigateBack() }
    BackHandler { handleBack() }
    val selectedDate by remember { mutableStateOf(LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(initialDateTime), ZoneId.systemDefault())) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateTime
    )

    val timePickerState = rememberTimePickerState(
        initialHour = selectedDate.hour,
        initialMinute = selectedDate.minute,
        is24Hour = false
    )
    Column(modifier = modifier.fillMaxSize()) {
        TopBar(title = "Select Date and Time", onClick = handleBack,
            showApply = true,
            onButtonClick = {
                val pickedDateMillis = datePickerState.selectedDateMillis
                if (pickedDateMillis != null) {
                    val zone = ZoneId.systemDefault()
                    val localDate =
                        java.time.Instant.ofEpochMilli(pickedDateMillis).atZone(zone)
                            .toLocalDate()
                    val finalDateTime =
                        localDate.atTime(timePickerState.hour, timePickerState.minute)
                    val finalMillis = finalDateTime.atZone(zone).toInstant().toEpochMilli()
                    log("finalMillis: ${finalMillis.toDateString()}")
                    onDateTimeSelected(finalMillis)
                }
            })
        Column(
            Modifier
                .fillMaxWidth()
                .background(secondaryBgColor)
                .verticalScroll(rememberScrollState())
                .padding(12.sdp)
        ) {
            VerticalSpacer(15)
            Text("Select  Date:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            DatePicker(state = datePickerState)

            VerticalSpacer(15)
            Text("Select Time:", style = SubtitleMedium, color = primaryColor)
            VerticalSpacer(12)
            TimePicker(state = timePickerState)

//            PrimaryButtonRounded(
//                modifier = Modifier.fillMaxWidth(),
//                gradientColors = listOf(secondaryColor, secondaryColor),
//                isRound = false,
//                onButtonClick = {
//                    val pickedDateMillis = datePickerState.selectedDateMillis
//                    if (pickedDateMillis != null) {
//                        val zone = ZoneId.systemDefault()
//                        val localDate =
//                            java.time.Instant.ofEpochMilli(pickedDateMillis).atZone(zone)
//                                .toLocalDate()
//                        val finalDateTime =
//                            localDate.atTime(timePickerState.hour, timePickerState.minute)
//                        val finalMillis = finalDateTime.atZone(zone).toInstant().toEpochMilli()
//                        log("finalMillis: ${finalMillis.toDateString()}")
//                        onDateTimeSelected(finalMillis)
//                    }
//                }
//            ) {
//                Text("Confirm", style = SubtitleLarge, color = white)
//            }
        }
    }
}
