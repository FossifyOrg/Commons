package org.fossify.commons.compose.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import org.fossify.commons.compose.extensions.BooleanPreviewParameterProvider
import org.fossify.commons.compose.extensions.MyDevices
import org.fossify.commons.compose.extensions.rememberMutableInteractionSource
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.compose.theme.SimpleTheme
import org.fossify.commons.compose.theme.preferenceLabelColor
import org.fossify.commons.compose.theme.preferenceValueColor

@Composable
fun SettingsCheckBoxComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: String? = null,
    initialValue: Boolean = false,
    isPreferenceEnabled: Boolean = true,
    onChange: ((Boolean) -> Unit)? = null,
    checkboxColors: CheckboxColors = CheckboxDefaults.colors(
        checkedColor = SimpleTheme.colorScheme.primary,
        checkmarkColor = SimpleTheme.colorScheme.surface,
    )
) {
    val interactionSource = rememberMutableInteractionSource()
    val indication = LocalIndication.current

    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = isPreferenceEnabled,
                onClick = { onChange?.invoke(!initialValue) },
                interactionSource = interactionSource,
                indication = indication
            ),
        colors = ListItemDefaults.colors(
            headlineColor = preferenceLabelColor(isEnabled = isPreferenceEnabled),
            supportingColor = preferenceValueColor(isEnabled = isPreferenceEnabled)
        ),
        headlineContent = {
            Text(text = label)
        },
        supportingContent = {
            AnimatedVisibility(visible = !value.isNullOrBlank()) {
                Text(text = value.toString())
            }
        },
        trailingContent = {
            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                Checkbox(
                    checked = initialValue,
                    onCheckedChange = { onChange?.invoke(it) },
                    enabled = isPreferenceEnabled,
                    colors = checkboxColors,
                    interactionSource = interactionSource
                )
            }
        }
    )
}

@MyDevices
@Composable
private fun SettingsCheckBoxComponentPreview(@PreviewParameter(BooleanPreviewParameterProvider::class) isChecked: Boolean) {
    var checked by remember { mutableStateOf(isChecked) }
    AppThemeSurface {
        SettingsCheckBoxComponent(
            label = "Some label",
            value = "Some value",
            initialValue = checked,
            onChange = {
                checked = it
            }
        )
    }
}
