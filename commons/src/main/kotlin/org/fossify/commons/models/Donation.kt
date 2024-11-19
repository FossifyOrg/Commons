package org.fossify.commons.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class Donation {
    data class Platform(
        val fee: Int,
        val link: String,
        @StringRes val nameRes: Int,
        @DrawableRes val iconRes: Int,
    ) : Donation()

    data class Crypto(
        val address: String,
        @StringRes val nameRes: Int,
        @DrawableRes val iconRes: Int,
    ) : Donation()
}
