package org.fossify.commons.compose.extensions

import android.app.Activity
import android.content.Context
import org.fossify.commons.R
import org.fossify.commons.extensions.baseConfig
import org.fossify.commons.extensions.redirectToRateUs
import org.fossify.commons.extensions.toast
import org.fossify.commons.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)

fun Activity.rateStarsRedirectAndThankYou(stars: Int) {
    if (stars == 5) {
        redirectToRateUs()
    }
    toast(R.string.thank_you)
    baseConfig.wasAppRated = true
}
