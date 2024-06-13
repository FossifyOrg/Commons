package org.fossify.commons.extensions

import android.view.View
import android.view.Window
import org.fossify.commons.helpers.DARK_GREY
import org.fossify.commons.helpers.isOreoPlus

fun Window.updateNavigationBarBackgroundColor(color: Int) {
    navigationBarColor = color
    updateNavigationBarForegroundColor(color)
}

fun Window.updateNavigationBarForegroundColor(color: Int) {
    if (isOreoPlus()) {
        if (color.getContrastColor() == DARK_GREY) {
            decorView.systemUiVisibility = decorView.systemUiVisibility.addBit(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility.removeBit(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        }
    }
}
