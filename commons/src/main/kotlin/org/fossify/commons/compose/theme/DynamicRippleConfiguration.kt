package org.fossify.commons.compose.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
internal object StateTokens {
    const val DraggedStateLayerOpacity = 0.16f
    const val FocusStateLayerOpacity = 0.12f
    const val HoverStateLayerOpacity = 0.08f
    const val PressedStateLayerOpacity = 0.12f
}

@Composable
fun dynamicRippleConfiguration() =
    RippleConfiguration(
        color = if (isSurfaceLitWell()) ripple_light else LocalContentColor.current,
        rippleAlpha = RippleAlpha(
            pressedAlpha = StateTokens.PressedStateLayerOpacity,
            focusedAlpha = StateTokens.FocusStateLayerOpacity,
            draggedAlpha = StateTokens.DraggedStateLayerOpacity,
            hoveredAlpha = StateTokens.HoverStateLayerOpacity
        )
    )
