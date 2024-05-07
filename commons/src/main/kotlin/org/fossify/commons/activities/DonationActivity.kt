package org.fossify.commons.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import org.fossify.commons.R
import org.fossify.commons.compose.extensions.enableEdgeToEdgeSimple
import org.fossify.commons.compose.screens.donation.DonationScreen
import org.fossify.commons.compose.screens.donation.FossifyCryptoAddresses
import org.fossify.commons.compose.screens.donation.FossifyDonationPlatforms
import org.fossify.commons.compose.theme.AppThemeSurface
import org.fossify.commons.extensions.openWebsiteIntent
import org.fossify.commons.extensions.toast

class DonationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            val clipboardManager = LocalClipboardManager.current
            AppThemeSurface {
                DonationScreen(
                    donationOptions = FossifyDonationPlatforms,
                    cryptoAddresses = FossifyCryptoAddresses,
                    goBack = ::finish,
                    openWebsite = ::openWebsiteIntent,
                    copyToClipboard = {
                        clipboardManager.setText(AnnotatedString(it))
                        toast(R.string.value_copied_to_clipboard)
                    },
                )
            }
        }
    }
}
