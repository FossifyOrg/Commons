package org.fossify.commons.compose.lists

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalLayoutDirection
import org.fossify.commons.compose.extensions.onEventValue
import org.fossify.commons.compose.system_ui_controller.rememberSystemUiController
import org.fossify.commons.compose.theme.LocalTheme
import org.fossify.commons.compose.theme.SimpleTheme
import org.fossify.commons.compose.theme.isNotLitWell
import org.fossify.commons.compose.theme.isSurfaceLitWell
import org.fossify.commons.compose.theme.model.Theme
import org.fossify.commons.extensions.getColoredMaterialStatusBarColor
import org.fossify.commons.extensions.getContrastColor

@Composable
internal fun SystemUISettingsScaffoldStatusBarColor(scrolledColor: Color) {
    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.statusBarDarkContentEnabled = scrolledColor.isNotLitWell()
        onDispose { }
    }
}

@Composable
internal fun ScreenBoxSettingsScaffold(paddingValues: PaddingValues, modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val layoutDirection = LocalLayoutDirection.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SimpleTheme.colorScheme.surface)
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection)
            )
    ) {
        content()
    }
}

@Composable
internal fun statusBarAndContrastColor(context: Context): Pair<Int, Color> {
    val statusBarColor = onEventValue { context.getColoredMaterialStatusBarColor() }
    val contrastColor by remember(statusBarColor) {
        derivedStateOf { Color(statusBarColor.getContrastColor()) }
    }
    return Pair(statusBarColor, contrastColor)
}

@Composable
internal fun transitionFractionAndScrolledColor(
    scrollBehavior: TopAppBarScrollBehavior,
    contrastColor: Color,
    darkIcons: Boolean = true,
): Pair<Float, Color> {
    val systemUiController = rememberSystemUiController()
    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
    val scrolledColor = lerp(
        start = if (isSurfaceLitWell()) Color.Black else Color.White,
        stop = contrastColor,
        fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
    )

    systemUiController.setStatusBarColor(
        color = Color.Transparent,
        darkIcons = scrolledColor.isNotLitWell() && darkIcons || (LocalTheme.current is Theme.SystemDefaultMaterialYou && !isSystemInDarkTheme())
    )
    return Pair(colorTransitionFraction, scrolledColor)
}
