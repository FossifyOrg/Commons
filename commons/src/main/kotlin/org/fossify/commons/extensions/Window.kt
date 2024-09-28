package org.fossify.commons.extensions

import android.view.View
import android.view.Window
import org.fossify.commons.helpers.DARK_GREY
import org.fossify.commons.helpers.isOreoPlus

fun Window.updateStatusBarColors(backgroundColor: Int) {
    statusBarColor = backgroundColor
    updateStatusBarForegroundColor(backgroundColor)
}

fun Window.updateStatusBarForegroundColor(backgroundColor: Int) {
    if (backgroundColor.getContrastColor() == DARK_GREY) {
        decorView.systemUiVisibility = decorView.systemUiVisibility.addBit(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    } else {
        decorView.systemUiVisibility = decorView.systemUiVisibility.removeBit(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }
}

fun Window.updateNavigationBarColors(backgroundColor: Int) {
    navigationBarColor = backgroundColor
    updateNavigationBarForegroundColor(backgroundColor)
}

fun Window.updateNavigationBarForegroundColor(backgroundColor: Int) {
    if (isOreoPlus()) {
        if (backgroundColor.getContrastColor() == DARK_GREY) {
            decorView.systemUiVisibility = decorView.systemUiVisibility.addBit(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility.removeBit(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        }
    }
}
