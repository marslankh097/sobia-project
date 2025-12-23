package com.example.budgetly.utils.shared_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.demo.budgetly.R
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.bgShade2
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.itemBgColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.textColor
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "Enter text here",
    inputType: KeyboardType = KeyboardType.Text,
    isMultiline: Boolean = false,
    backgroundColor: Color = itemBgColor,
    borderColor: Color = bgShade2,
    iconCrossClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    val textFieldModifier = if (isMultiline) {
        Modifier
            .fillMaxWidth()
            .heightIn(min = 120.sdp)
            .verticalScroll(scrollState)
    } else {
        Modifier
            .fillMaxWidth()
    }

    RoundedBorderRectangle(
        modifier = modifier,
        bgColor = backgroundColor,
        borderColor = borderColor,
        borderRadius = 12f,
        borderThickness = 1f
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.sdp, vertical = if (isMultiline) 10.sdp else 0.sdp),
            verticalAlignment = if (!isMultiline) Alignment.CenterVertically else Alignment.Top
        ) {
            TextField(
                value = text,
                onValueChange = onValueChange,
                singleLine = !isMultiline,
                textStyle = SubtitleLarge.copy(color = textColor),
                placeholder = {
                    Text(
                        text = hint,
                        style = SubtitleLarge.copy(color = hintColor)
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = inputType,
                    imeAction = if (isMultiline) ImeAction.Default else ImeAction.Done
                ),
                modifier = textFieldModifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = hintColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            if (text.trim().isNotEmpty()) {
                Image(
                    modifier = Modifier
                        .size(20.sdp)
                        .padding(start = 8.sdp)
                        .safeClickAble { iconCrossClick() },
                    painter = painterResource(id = R.drawable.icon_cross),
                    contentDescription = stringResource(R.string.clear_search_bar)
                )
            }
        }
    }
}
