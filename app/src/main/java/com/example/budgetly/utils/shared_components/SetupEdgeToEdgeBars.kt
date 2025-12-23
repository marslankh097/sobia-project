package com.example.budgetly.utils.shared_components

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.demo.budgetly.R

@Composable
fun SetupEdgeToEdgeBars(
    activity: Activity,
    isDarkTheme: Boolean = false
) {
    val window = activity.window

    // Allow your app to draw behind system bars
    WindowCompat.setDecorFitsSystemWindows(window, false)

    // Set status bar and nav bar colors
//    val backgroundColor = Color.Transparent // or use theme surface if needed
    val backgroundColor = ContextCompat.getColor(activity, R.color.white)
    window.statusBarColor = backgroundColor
    window.navigationBarColor = backgroundColor

    // Control icon color (light/dark icons)
    val insetsController = WindowInsetsControllerCompat(window, window.decorView)
    insetsController.isAppearanceLightStatusBars = !isDarkTheme
    insetsController.isAppearanceLightNavigationBars = !isDarkTheme
}