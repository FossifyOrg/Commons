package org.fossify.commons.compose.theme

import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable

@Composable
fun dynamicRippleConfiguration() =
    RippleConfiguration(
        color = if (isSurfaceLitWell()) ripple_light else ripple_dark,
    )
