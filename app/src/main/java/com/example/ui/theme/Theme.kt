package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.model.ThemeStyle

/**
 * App theme wrapper.
 *
 * [themeStyle] selects one of the four palettes; it is resolved from the persisted Settings value,
 * so the whole app re-colours instantly when the user picks a different theme.
 */
@Composable
fun MyApplicationTheme(
    themeStyle: ThemeStyle = ThemeStyle.KUMARTULI_TEAL,
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val palette = themeStyle.palette

    CompositionLocalProvider(LocalAppPalette provides palette) {
        MaterialTheme(
            colorScheme = palette.toColorScheme(),
            typography = Typography,
            content = content
        )
    }
}
