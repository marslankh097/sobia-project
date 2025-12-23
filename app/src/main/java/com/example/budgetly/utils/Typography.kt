package com.example.budgetly.utils

import com.demo.budgetly.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


val robotoFamily = FontFamily(
    Font(resId = R.font.roboto_bold, weight = FontWeight.Bold),
    Font(resId = R.font.roboto_regular, weight = FontWeight.Normal),
    Font(resId = R.font.roboto_medium, weight = FontWeight.Medium),
    Font(resId = R.font.roboto_medium, weight = FontWeight.SemiBold)
)

val TypoH1=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 38.sp)

val TypoH2=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 34.sp)

val TypoH3=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 30.sp)

val TypoH4=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 25.sp)

val TypoH5=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 21.sp)

val TypoH6=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp)

val TypoH7=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)



val SubtitleLarge=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

val SubtitleMedium=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)

val SubtitleSmall=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)



val BodyLarge= androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp)

val BodyMedium= androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp)

val BodySmall= androidx.compose.ui.text.TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp
)



val LabelLarge= androidx.compose.ui.text.TextStyle(
    fontFamily = robotoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)

val LabelMedium=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Normal, fontSize = 10.sp)

val LabelSmall=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Normal, fontSize = 8.sp)



val Caption1=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Normal, fontSize = 10.sp)

val Caption2=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Normal, fontSize = 8.sp)



val ButtonLarge=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp)

val ButtonMedium=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp)

val ButtonSmall=androidx.compose.ui.text.TextStyle(fontFamily = robotoFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp)