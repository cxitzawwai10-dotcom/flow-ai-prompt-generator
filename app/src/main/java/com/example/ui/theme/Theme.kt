package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FlowColorScheme = darkColorScheme(
    primary = FlowCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF97F0FF),
    secondary = FlowPurple,
    onSecondary = Color(0xFF381E72),
    secondaryContainer = Color(0xFF4F378B),
    onSecondaryContainer = Color(0xFFE8DDFF),
    tertiary = FlowCoral,
    onTertiary = Color(0xFF5A1A28),
    tertiaryContainer = Color(0xFF752435),
    onTertiaryContainer = Color(0xFFFFD9DF),
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark,
    outlineVariant = Color(0xFF334155)
)

@Composable
fun FlowAiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FlowColorScheme,
        typography = AppTypography,
        content = content
    )
}
