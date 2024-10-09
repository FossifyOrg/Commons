package org.fossify.commons.compose.screens.donation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import org.fossify.commons.R

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

val FossifyDonationPlatforms = listOf(
    Donation.Platform(
        fee = 0,
        link = "https://github.com/sponsors/FossifyOrg",
        nameRes = R.string.github_sponsors,
        iconRes = R.drawable.ic_github_tinted_vector
    ),
    Donation.Platform(
        fee = 0,
        link = "https://liberapay.com/naveensingh",
        nameRes = R.string.liberapay,
        iconRes = R.drawable.ic_liberapay_vector
    ),
    Donation.Platform(
        fee = 10,
        link = "https://opencollective.com/fossify/donate?interval=month&amount=20",
        nameRes = R.string.opencollective,
        iconRes = R.drawable.ic_open_collective_vector
    ),
    Donation.Platform(
        fee = 10,
        link = "https://www.patreon.com/naveen3singh",
        nameRes = R.string.patreon,
        iconRes = R.drawable.ic_patreon_vector
    ),
    Donation.Platform(
        fee = 5,
        link = "https://paypal.me/naveen3singh",
        nameRes = R.string.paypal,
        iconRes = R.drawable.ic_paypal_vector
    ),
)

@Suppress("SpellCheckingInspection")
val FossifyCryptoAddresses = listOf(
    Donation.Crypto(
        address = "bc1qn5h97qdqsazpzvxm7gryke6vmrcx85t7neqp95",
        iconRes = R.drawable.ic_bitcoin_vector,
        nameRes = R.string.bitcoin_btc
    ),
    Donation.Crypto(
        address = "0x9354fC372BC3BdA58766a8a9Fabadf77A76CdE01",
        iconRes = R.drawable.ic_ethereum_vector,
        nameRes = R.string.ethereum_eth
    ),
    Donation.Crypto(
        address = "48FkVUcJ7AGeBMR4SC4J7QU5nAt6YNwKZWz6sGDT1s5haEY7reZtJr5CniXLaQzTzGAuZNoc83BQAcETHw1d3Lkn8AAf1XF",
        iconRes = R.drawable.ic_monero_vector,
        nameRes = R.string.monero_xmr
    ),
    Donation.Crypto(
        address = "TGi4VpD1D9A9ZvyP9d3aVowwzMSvev2hub",
        iconRes = R.drawable.ic_tron_vector,
        nameRes = R.string.tron_trx
    )
)
