package com.example.budgetly.utils.shared_components
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.budgetly.utils.primaryColor

@Composable
fun HighlightedText(modifier: Modifier = Modifier, fullText: String, style: TextStyle, textColor: Color, searchText: String, highlightColor:Color = primaryColor, textAlign: TextAlign = TextAlign.Start ) {
    val annotatedString = remember(fullText, searchText) {
        buildAnnotatedString {
            val startIndex = fullText.indexOf(searchText, ignoreCase = true)
            if (startIndex >= 0 && searchText.isNotBlank()) {
                append(fullText.substring(0, startIndex))
                withStyle(style = SpanStyle(background = highlightColor, color = Color.White)) {
                    append(fullText.substring(startIndex, startIndex + searchText.length))
                }
                append(fullText.substring(startIndex + searchText.length))
            } else {
                append(fullText)
            }
        }
    }
    Text(modifier = modifier, text = annotatedString, style = style, color = textColor, textAlign = textAlign)
}
