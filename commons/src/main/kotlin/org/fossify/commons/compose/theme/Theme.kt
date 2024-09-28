package org.fossify.commons.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import org.fossify.commons.compose.extensions.config
import org.fossify.commons.compose.theme.model.Theme
import org.fossify.commons.compose.theme.model.Theme.Companion.systemDefaultMaterialYou
import org.fossify.commons.extensions.getContrastColor
import org.fossify.commons.helpers.isSPlus

@Composable
internal fun Theme(
    theme: Theme = systemDefaultMaterialYou(),
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val baseConfig = remember { context.config }
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val colorScheme = if (!view.isInEditMode) {
        when {
            theme is Theme.SystemDefaultMaterialYou && isSPlus() -> {
                if (isSystemInDarkTheme) {
                    dynamicDarkColorScheme(context)
                } else {
                    dynamicLightColorScheme(context)
                }
            }

            theme is Theme.Custom && theme.backgroundColor.isLitWell() -> lightColorScheme(
                primary = theme.primaryColor,
                onPrimary = Color(theme.primaryColorInt.getContrastColor()),
                surface = theme.backgroundColor,
                onSurface = theme.textColor,
                surfaceVariant = theme.backgroundColor
            )

            theme is Theme.Custom -> darkColorScheme(
                primary = theme.primaryColor,
                onPrimary = Color(theme.primaryColorInt.getContrastColor()),
                surface = theme.backgroundColor,
                onSurface = theme.textColor,
                surfaceVariant = theme.backgroundColor
            )

            theme is Theme.Dark -> darkColorScheme(
                primary = theme.primaryColor,
                onPrimary = Color(theme.primaryColorInt.getContrastColor()),
                surface = theme.backgroundColor,
                onSurface = theme.textColor
            )

            theme is Theme.White -> lightColorScheme(
                primary = Color(theme.accentColor),
                onPrimary = Color(theme.accentColor.getContrastColor()),
                surface = theme.backgroundColor,
                tertiary = theme.primaryColor,
                onSurface = theme.textColor
            )

            theme is Theme.BlackAndWhite -> darkColorScheme(
                primary = Color(theme.accentColor),
                onPrimary = Color(theme.accentColor.getContrastColor()),
                surface = theme.backgroundColor,
                tertiary = theme.primaryColor,
                onSurface = theme.textColor
            )

            else -> darkColorScheme
        }
    } else {
        previewColorScheme()
    }

    SideEffect {
        updateRecentsAppIcon(baseConfig, context)
    }

    val dimensions = CommonDimensions

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        content = {
            CompositionLocalProvider(
                LocalRippleConfiguration provides dynamicRippleConfiguration(),
                LocalTheme provides theme,
                LocalDimensions provides dimensions
            ) {
                content()
            }
        },
    )
}

val LocalTheme: ProvidableCompositionLocal<Theme> =
    staticCompositionLocalOf { Theme.Custom(1, 1, 1, 1) }

@Composable
private fun previewColorScheme() = if (isSystemInDarkTheme()) {
    darkColorScheme
} else {
    lightColorScheme
}

