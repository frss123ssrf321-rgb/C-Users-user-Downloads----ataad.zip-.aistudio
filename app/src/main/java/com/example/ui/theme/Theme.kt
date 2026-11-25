package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private val AtaadLightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = TextOnNavy,
    primaryContainer = NavyLight,
    onPrimaryContainer = TextOnNavy,
    secondary = GoldAccent,
    onSecondary = NavyPrimary,
    secondaryContainer = GoldSurface,
    onSecondaryContainer = NavyPrimary,
    tertiary = VerifiedGreen,
    onTertiary = CardWhite,
    tertiaryContainer = VerifiedGreenBg,
    onTertiaryContainer = VerifiedGreen,
    background = BgLight,
    onBackground = TextPrimary,
    surface = CardWhite,
    onSurface = TextPrimary,
    surfaceVariant = BgLight,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder,
    outlineVariant = DividerLight,
    error = AlertRed,
    onError = CardWhite,
    errorContainer = AlertRedBg,
    onErrorContainer = AlertRed
)

val AtaadShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun AtaadTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = AtaadLightColorScheme,
            typography = AtaadTypography,
            shapes = AtaadShapes,
            content = content
        )
    }
}
