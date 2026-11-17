package com.secondspace.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Always-light Material 3 theme (matches the light framework theme in themes.xml). */
@Composable
fun SecondSpaceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color(0xFF1F4FD8)),
        content = content
    )
}
