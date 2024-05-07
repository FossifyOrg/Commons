package org.fossify.commons.compose.screens.donation

import org.fossify.commons.R

sealed class Donation {
    data class Platform(
        val fee: Int,
        val icon: Int,
        val link: String,
        val name: String,
    ) : Donation()

    data class Crypto(
        val address: String,
        val icon: Int,
        val name: String,
    ) : Donation()
}

val FossifyDonationPlatforms = listOf(
    Donation.Platform(
        fee = 0,
        icon = R.drawable.ic_github_tinted_vector,
        link = "https://github.com/sponsors/FossifyOrg",
        name = "GitHub Sponsors"
    ),
    Donation.Platform(
        fee = 0,
        icon = R.drawable.ic_liberapay_vector,
        link = "https://liberapay.com/naveensingh",
        name = "Liberapay"
    ),
    Donation.Platform(
        fee = 10,
        icon = R.drawable.ic_open_collective_vector,
        link = "https://opencollective.com/fossify/donate?interval=month&amount=20",
        name = "OpenCollective"
    ),
    Donation.Platform(
        fee = 10,
        icon = R.drawable.ic_patreon_vector,
        link = "https://www.patreon.com/naveen3singh",
        name = "Patreon"
    ),
    Donation.Platform(
        fee = 5,
        icon = R.drawable.ic_paypal_vector,
        link = "https://paypal.me/naveen3singh",
        name = "PayPal"
    ),
)

@Suppress("SpellCheckingInspection")
val FossifyCryptoAddresses = listOf(
    Donation.Crypto(
        address = "bc1qn5h97qdqsazpzvxm7gryke6vmrcx85t7neqp95",
        icon = R.drawable.ic_bitcoin_vector,
        name = "Bitcoin (BTC)"
    ),
    Donation.Crypto(
        address = "0x9354fC372BC3BdA58766a8a9Fabadf77A76CdE01",
        icon = R.drawable.ic_ethereum_vector,
        name = "Ethereum (ETH)"
    ),
    Donation.Crypto(
        address = "48FkVUcJ7AGeBMR4SC4J7QU5nAt6YNwKZWz6sGDT1s5haEY7reZtJr5CniXLaQzTzGAuZNoc83BQAcETHw1d3Lkn8AAf1XF",
        icon = R.drawable.ic_monero_vector,
        name = "Monero (XMR)"
    ),
    Donation.Crypto(
        address = "TGi4VpD1D9A9ZvyP9d3aVowwzMSvev2hub",
        icon = R.drawable.ic_tron_vector,
        name = "Tron (TRX)"
    )
)
