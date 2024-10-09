package org.fossify.commons.compose.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import org.fossify.commons.R
import org.fossify.commons.compose.extensions.BooleanPreviewParameterProvider
import org.fossify.commons.compose.extensions.MyDevices
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.compose.theme.SimpleTheme
import org.fossify.commons.compose.theme.textSubTitleColor

@Composable
fun SettingsListItem(
    modifier: Modifier = Modifier,
    text: String,
    description: String? = null,
    fontSize: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    @DrawableRes icon: Int? = null,
    isImage: Boolean = false,
    click: (() -> Unit)? = null,
    tint: Color? = null
) {
    ListItem(
        headlineContent = {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(modifier),
                fontSize = fontSize,
                maxLines = maxLines,
                overflow = overflow
            )
        },
        leadingContent = {
            val imageSize = Modifier
                .size(SimpleTheme.dimens.icon.extraSmall)
            when {
                icon != null && isImage && tint != null -> Image(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    colorFilter = ColorFilter.tint(tint)
                )

                icon != null && isImage && tint == null -> Image(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = text,
                )

                icon != null && !isImage && tint == null -> Icon(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = text,
                )

                icon != null && !isImage && tint != null -> Icon(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    tint = tint
                )

                else -> Box(
                    modifier = imageSize,
                )
            }
        },
        supportingContent = description?.let {
            { Text(text = description, color = textSubTitleColor) }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = click != null) { click?.invoke() }
    )
}


@Composable
fun SettingsListItem(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    @DrawableRes icon: Int? = null,
    isImage: Boolean = false,
    click: (() -> Unit)? = null,
    tint: Color? = null
) {
    ListItem(
        headlineContent = {
            text()
        },
        leadingContent = {
            val imageSize = Modifier.size(SimpleTheme.dimens.icon.extraSmall)
            when {
                icon != null && isImage && tint != null -> Image(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(tint)
                )

                icon != null && isImage && tint == null -> Image(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )

                icon != null && !isImage && tint == null -> Icon(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )

                icon != null && !isImage && tint != null -> Icon(
                    modifier = imageSize,
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = tint
                )

                else -> Box(
                    modifier = imageSize,
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = click != null) { click?.invoke() }
    )
}


@MyDevices
@Composable
private fun PreviewSettingsListItem(@PreviewParameter(BooleanPreviewParameterProvider::class) isImage: Boolean) {
    AppThemeSurface {
        SettingsListItem(
            click = {},
            text = "Fossify",
            icon = if (isImage) R.drawable.ic_telegram_vector else R.drawable.ic_dollar_vector,
            isImage = isImage
        )
    }
}
