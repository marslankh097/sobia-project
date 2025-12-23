package com.example.budgetly.utils.shared_components
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.demo.budgetly.R
import com.example.budgetly.utils.HorizontalSpacer
import com.example.budgetly.utils.SubtitleLarge
import com.example.budgetly.utils.hintColor
import com.example.budgetly.utils.safeClickAble
import com.example.budgetly.utils.textColor

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    hint: String = "Search Category Name here",
    backgroundColor: Color,
    borderColor: Color,
    iconCrossClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    RoundedBorderRectangle(
        modifier = modifier.padding(horizontal = 12.dp),
        backgroundColor,
        borderColor,
        12f,
        1f
    ) {
        Row(
            modifier = modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.icon_search_small),
                contentDescription = ""
            )
            TextField(
                singleLine = true,
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f),
                textStyle = SubtitleLarge.copy(color = textColor),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = hintColor,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                value = text, onValueChange = { onValueChange.invoke(it) },
                placeholder = {
                    Text(
                        text = hint,
                        style = SubtitleLarge.copy(color = hintColor)
                    )
                })
//            Spacer(modifier = Modifier.weight(1f))
            if (text.trim().isNotEmpty()) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp)
                        .safeClickAble {
                            iconCrossClick.invoke()
                        },
                    painter = painterResource(id = R.drawable.icon_cross),
                    contentDescription = stringResource(R.string.clear_search_bar)
                )
                HorizontalSpacer(8)
            }
        }
    }
}
