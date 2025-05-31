package org.fossify.commons.compose.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import org.fossify.commons.R
import org.fossify.commons.compose.extensions.MyDevices
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.compose.theme.preferenceLabelColor
import org.fossify.commons.compose.theme.preferenceValueColor

@Composable
fun SettingsPreferenceComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: String? = null,
    isPreferenceEnabled: Boolean = true,
    doOnPreferenceLongClick: (() -> Unit)? = null,
    doOnPreferenceClick: (() -> Unit)? = null,
    preferenceValueColor: Color = preferenceValueColor(isEnabled = isPreferenceEnabled),
    preferenceLabelColor: Color = preferenceLabelColor(isEnabled = isPreferenceEnabled)
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                enabled = isPreferenceEnabled,
                onClick = { doOnPreferenceClick?.invoke() },
                onLongClick = { doOnPreferenceLongClick?.invoke() },
            )
            .then(modifier),
        headlineContent = {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        supportingContent = {
            AnimatedVisibility(visible = !value.isNullOrBlank()) {
                Text(
                    text = value.toString(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        colors = ListItemDefaults.colors(
            headlineColor = preferenceLabelColor,
            supportingColor = preferenceValueColor
        )
    )
}

@MyDevices
@Composable
private fun SettingsPreferencePreview() {
    AppThemeSurface {
        SettingsPreferenceComponent(
            label = stringResource(id = R.string.language),
            value = stringResource(id = R.string.translation_english),
            isPreferenceEnabled = true,
        )
    }
}
