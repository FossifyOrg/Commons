package org.fossify.commons.extensions

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewTreeObserver
import org.fossify.commons.R
import org.fossify.commons.helpers.SHORT_ANIMATION_DURATION

fun View.beInvisibleIf(beInvisible: Boolean) = if (beInvisible) beInvisible() else beVisible()

fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()

fun View.beGoneIf(beGone: Boolean) = beVisibleIf(!beGone)

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}

fun View.onGlobalLayout(callback: () -> Unit) {
    viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (viewTreeObserver != null) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback()
            }
        }
    })
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.isGone() = visibility == View.GONE

fun View.performHapticFeedback() = performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)

fun View.fadeIn(duration: Long = SHORT_ANIMATION_DURATION) {
    animate().alpha(1f).setDuration(duration).withStartAction { beVisible() }.start()
}

fun View.fadeOut(duration: Long = SHORT_ANIMATION_DURATION) {
    animate().alpha(0f).setDuration(duration).withEndAction { beGone() }.start()
}

fun View.setupViewBackground(context: Context) {
    background = if (context.isDynamicTheme()) {
        resources.getDrawable(R.drawable.selector_clickable_you)
    } else {
        resources.getDrawable(R.drawable.selector_clickable)
    }
}

/**
 * Sets a click listener that prevents quick repeated clicks.
 */
fun View.setDebouncedClickListener(
    debounceInterval: Long = 500,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceInterval) {
            lastClickTime = currentTime
            onClick(it)
        }
    }
}
