package com.example.budgetly.ads

import com.demo.budgetly.R

enum class NativeLayouts(
    val layout: String,
    val shimmer: Int,
) {
    SmallButtonNative("small_button_native_layout", R.layout.small_button_native_layout),
    SmallNative("small_native_layout", R.layout.small_native_layout),
    SplitNative("jazz_native_layout", R.layout.jazz_native_layout),
    LargeNative("large_native_layout", R.layout.large_native_layout)
}