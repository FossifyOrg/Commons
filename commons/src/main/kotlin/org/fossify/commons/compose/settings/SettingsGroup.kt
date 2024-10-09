package org.fossify.commons.compose.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.fossify.commons.compose.extensions.MyDevices
import org.fossify.commons.compose.theme.SimpleTheme

@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (title != null) {
            SettingsGroupTitle(title = title)
        }
        content()
    }
}

@Composable
fun SettingsGroupTitle(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SimpleTheme.dimens.padding.extraLarge),
        contentAlignment = Alignment.CenterStart
    ) {
        val primary = SimpleTheme.colorScheme.primary
        val titleStyle = SimpleTheme.typography.headlineMedium.copy(color = primary)
        ProvideTextStyle(value = titleStyle) { title() }
    }
}

@MyDevices
@Composable
private fun SettingsGroupPreview() {
    MaterialTheme {
        SettingsGroup(
            title = { Text(text = "Title", modifier = Modifier.padding(start = 40.dp)) }
        ) {
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(text = "Settings group")
                }
            )
        }
    }
}
