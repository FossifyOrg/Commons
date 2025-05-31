package org.fossify.commons.compose.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.fossify.commons.R
import org.fossify.commons.compose.extensions.MyDevices
import org.fossify.commons.compose.extensions.rememberMutableInteractionSource
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.compose.theme.SimpleTheme

@Composable
fun SimpleScaffoldTopBar(
    modifier: Modifier = Modifier,
    title: String,
    scrolledColor: Color,
    navigationIconInteractionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    statusBarColor: Int,
    colorTransitionFraction: Float,
    contrastColor: Color,
    goBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = scrolledColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            SimpleNavigationIcon(
                goBack = goBack,
                navigationIconInteractionSource = navigationIconInteractionSource,
                iconColor = scrolledColor
            )
        },
        scrollBehavior = scrollBehavior,
        colors = simpleTopAppBarColors(statusBarColor, colorTransitionFraction, contrastColor),
        modifier = modifier.topAppBarPaddings(),
        windowInsets = topAppBarInsets()
    )
}

@Composable
fun SimpleScaffoldTopBar(
    modifier: Modifier = Modifier,
    title: @Composable (scrolledColor: Color) -> Unit,
    scrolledColor: Color,
    navigationIconInteractionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    statusBarColor: Int,
    colorTransitionFraction: Float,
    contrastColor: Color,
    goBack: () -> Unit,
) {
    TopAppBar(
        title = {
            title(scrolledColor)
        },
        navigationIcon = {
            SimpleNavigationIcon(
                goBack = goBack,
                navigationIconInteractionSource = navigationIconInteractionSource,
                iconColor = scrolledColor
            )
        },
        scrollBehavior = scrollBehavior,
        colors = simpleTopAppBarColors(statusBarColor, colorTransitionFraction, contrastColor),
        modifier = modifier.topAppBarPaddings(),
        windowInsets = topAppBarInsets()
    )
}

@Composable
fun SimpleScaffoldTopBar(
    modifier: Modifier = Modifier,
    title: @Composable (scrolledColor: Color) -> Unit,
    actions: @Composable RowScope.() -> Unit,
    scrolledColor: Color,
    navigationIconInteractionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    statusBarColor: Int,
    colorTransitionFraction: Float,
    contrastColor: Color,
    goBack: () -> Unit,
) {
    TopAppBar(
        title = {
            title(scrolledColor)
        },
        navigationIcon = {
            SimpleNavigationIcon(
                goBack = goBack,
                navigationIconInteractionSource = navigationIconInteractionSource,
                iconColor = scrolledColor
            )
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = simpleTopAppBarColors(statusBarColor, colorTransitionFraction, contrastColor),
        modifier = modifier.topAppBarPaddings(),
        windowInsets = topAppBarInsets()
    )
}

@Composable
fun simpleTopAppBarColors(
    statusBarColor: Int,
    colorTransitionFraction: Float,
    contrastColor: Color
) = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = Color(statusBarColor),
    containerColor = if (colorTransitionFraction == 1f) contrastColor else SimpleTheme.colorScheme.surface,
    navigationIconContentColor = if (colorTransitionFraction == 1f) contrastColor else SimpleTheme.colorScheme.surface
)

@Composable
fun topAppBarInsets() = TopAppBarDefaults.windowInsets.exclude(WindowInsets.navigationBars)

@Composable
fun Modifier.topAppBarPaddings(
    paddingValues: PaddingValues = WindowInsets.navigationBars.asPaddingValues()
): Modifier {
    val layoutDirection = LocalLayoutDirection.current
    return padding(
        top = paddingValues.calculateTopPadding(),
        start = paddingValues.calculateStartPadding(layoutDirection),
        end = paddingValues.calculateEndPadding(layoutDirection)
    )
}

@Composable
fun SimpleNavigationIcon(
    modifier: Modifier = Modifier,
    navigationIconInteractionSource: MutableInteractionSource = rememberMutableInteractionSource(),
    goBack: () -> Unit,
    iconColor: Color? = null
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .padding(SimpleTheme.dimens.padding.small)
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = navigationIconInteractionSource,
                indication = ripple(
                    color = SimpleTheme.colorScheme.onSurface,
                    bounded = true
                ),
                onClick = goBack
            ),
        contentAlignment = Alignment.Center
    ) {
        SimpleBackIcon(iconColor)
    }
}

@Composable
fun SimpleBackIcon(iconColor: Color?) {
    if (iconColor == null) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back),
            modifier = Modifier.size(SimpleTheme.dimens.icon.extraSmall)
        )
    } else {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back),
            tint = iconColor,
            modifier = Modifier.size(SimpleTheme.dimens.icon.extraSmall)
        )
    }
}


@Composable
@MyDevices
private fun SimpleScaffoldTopBarPreview() {
    AppThemeSurface {
        SimpleScaffoldTopBar(
            title = "SettingsScaffoldTopBar",
            scrolledColor = Color.Black,
            navigationIconInteractionSource = rememberMutableInteractionSource(),
            goBack = {},
            statusBarColor = Color.Magenta.toArgb(),
            colorTransitionFraction = 1.0f,
            contrastColor = Color.White
        )
    }
}
