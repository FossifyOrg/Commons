package org.fossify.commons.compose.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fossify.commons.compose.extensions.BooleanPreviewParameterProvider
import org.fossify.commons.compose.extensions.MyDevices
import org.fossify.commons.compose.extensions.NoRippleTheme
import org.fossify.commons.compose.extensions.rememberMutableInteractionSource
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.compose.theme.SimpleTheme
import org.fossify.commons.compose.theme.preferenceLabelColor
import org.fossify.commons.compose.theme.preferenceValueColor

@Composable
fun SettingsSwitchComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: String? = null,
    initialValue: Boolean = false,
    isPreferenceEnabled: Boolean = true,
    showCheckmark: Boolean = false,
    onChange: ((Boolean) -> Unit)? = null,
    switchColors: SwitchColors = SwitchDefaults.colors()
) {
    val interactionSource = rememberMutableInteractionSource()
    val indication = LocalIndication.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onChange?.invoke(!initialValue) },
                interactionSource = interactionSource,
                indication = indication
            )
            .padding(horizontal = 20.dp, vertical = 6.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = SimpleTheme.dimens.padding.extraLarge),
                text = label,
                color = preferenceLabelColor(isEnabled = isPreferenceEnabled),
                fontSize = 14.sp
            )
            AnimatedVisibility(visible = !value.isNullOrBlank()) {
                Text(
                    text = value.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = SimpleTheme.dimens.padding.extraLarge),
                    color = preferenceValueColor(isEnabled = isPreferenceEnabled),
                )
            }
        }
        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            Switch(
                checked = initialValue,
                onCheckedChange = { onChange?.invoke(it) },
                thumbContent = if (showCheckmark && initialValue) {
                    {
                        Icon(
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = SimpleTheme.colorScheme.primary
                        )
                    }
                } else null,
                enabled = isPreferenceEnabled,
                colors = switchColors,
                interactionSource = interactionSource,
            )
        }
    }
}

@MyDevices
@Composable
private fun SettingsSwitchComponentPreview(@PreviewParameter(BooleanPreviewParameterProvider::class) isChecked: Boolean) {
    AppThemeSurface {
        SettingsSwitchComponent(
            label = "Some label",
            value = "Some value",
            initialValue = isChecked
        )
    }
}
