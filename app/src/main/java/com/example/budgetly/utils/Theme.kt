package com.example.budgetly.utils
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

//Typography
val Typography = Typography(
    labelLarge = LabelLarge,
    labelMedium = LabelMedium,
    labelSmall = LabelSmall,

    bodyLarge = BodyLarge,
    bodyMedium = BodyMedium,
    bodySmall = BodySmall,

    titleLarge = SubtitleLarge,
    titleMedium =SubtitleMedium,
    titleSmall = SubtitleSmall,

    headlineLarge = TypoH4,
    headlineMedium =TypoH5,
    headlineSmall = TypoH6,

    displayLarge = LabelLarge,
    displayMedium =LabelLarge,
    displaySmall = LabelLarge)


private fun getLightColorScheme(appThemePosition: Int): ColorScheme {
    return lightColorScheme(
        primary = primaryColor,
        secondary = btnGradientColor1,
        tertiary = btnGradientColor2)
}
private fun getDarkColorScheme(appThemePosition: Int): ColorScheme {
    return darkColorScheme(
        primary = primaryColor,
        secondary = btnGradientColor1,
        tertiary = btnGradientColor2)
}

@Composable
fun BudgetAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    isDarkMode:Boolean = false,
    themePosition:Int = 0,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkMode -> getDarkColorScheme(themePosition)
        else -> getLightColorScheme(themePosition)
    }
    val view = LocalView.current
    val context = LocalContext.current
    val activity = context as? Activity
    val window = activity?.window
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
    SideEffect {
        if (window != null) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val insetsController = WindowInsetsControllerCompat(window, view)
            insetsController.isAppearanceLightStatusBars = isDarkMode.not()
            window.statusBarColor = itemBgColor.toArgb()
        }
    }
}