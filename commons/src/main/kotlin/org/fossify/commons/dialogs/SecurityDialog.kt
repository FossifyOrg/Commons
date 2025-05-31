package org.fossify.commons.dialogs

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.biometric.auth.AuthPromptHost
import androidx.fragment.app.FragmentActivity
import org.fossify.commons.R
import org.fossify.commons.adapters.PasswordTypesAdapter
import org.fossify.commons.databinding.DialogSecurityBinding
import org.fossify.commons.extensions.*
import org.fossify.commons.helpers.*
import org.fossify.commons.interfaces.HashListener
import org.fossify.commons.views.MyDialogViewPager

class SecurityDialog(
    private val activity: Activity,
    private val requiredHash: String,
    private val showTabIndex: Int,
    private val callback: (hash: String, type: Int, success: Boolean) -> Unit
) : HashListener {
    private var dialog: AlertDialog? = null
    private val binding = DialogSecurityBinding.inflate(LayoutInflater.from(activity), null, false)
    private var tabsAdapter: PasswordTypesAdapter
    private var viewPager: MyDialogViewPager

    init {
        binding.apply {
            viewPager = dialogTabViewPager
            viewPager.offscreenPageLimit = 2
            tabsAdapter = PasswordTypesAdapter(
                context = root.context,
                requiredHash = requiredHash,
                hashListener = this@SecurityDialog,
                scrollView = dialogScrollview,
                biometricPromptHost = AuthPromptHost(activity as FragmentActivity),
                showBiometricIdTab = activity.isBiometricAuthSupported(),
                showBiometricAuthentication = showTabIndex == PROTECTION_FINGERPRINT && isRPlus()
            )
            viewPager.adapter = tabsAdapter
            viewPager.onPageChangeListener {
                dialogTabLayout.getTabAt(it)?.select()
            }

            viewPager.onGlobalLayout {
                updateTabVisibility()
            }

            if (showTabIndex == SHOW_ALL_TABS) {
                val textColor = root.context.getProperTextColor()

                if (activity.isBiometricAuthSupported()) {
                    val tabTitle = if (isRPlus()) R.string.biometrics else R.string.fingerprint
                    dialogTabLayout.addTab(dialogTabLayout.newTab().setText(tabTitle), PROTECTION_FINGERPRINT)
                }

                if (activity.isDynamicTheme()) {
                    dialogTabLayout.setBackgroundColor(activity.resources.getColor(R.color.you_dialog_background_color))
                } else {
                    dialogTabLayout.setBackgroundColor(root.context.getProperBackgroundColor())
                }

                dialogTabLayout.setTabTextColors(textColor, textColor)
                dialogTabLayout.setSelectedTabIndicatorColor(root.context.getProperPrimaryColor())
                dialogTabLayout.onTabSelectionChanged(tabSelectedAction = {
                    viewPager.currentItem = when {
                        it.text.toString().equals(root.context.resources.getString(R.string.pattern), true) -> PROTECTION_PATTERN
                        it.text.toString().equals(root.context.resources.getString(R.string.pin), true) -> PROTECTION_PIN
                        else -> PROTECTION_FINGERPRINT
                    }
                    updateTabVisibility()
                })
            } else {
                dialogTabLayout.beGone()
                viewPager.currentItem = showTabIndex
                viewPager.allowSwiping = false
            }
        }

        activity.getAlertDialogBuilder()
            .setOnCancelListener { onCancelFail() }
            .setNegativeButton(R.string.cancel) { _, _ -> onCancelFail() }
            .apply {
                activity.setupDialogStuff(binding.root, this) { alertDialog ->
                    dialog = alertDialog
                }
            }
    }

    private fun onCancelFail() {
        callback("", 0, false)
        dialog?.dismiss()
    }

    override fun receivedHash(hash: String, type: Int) {
        callback(hash, type, true)
        if (!activity.isFinishing) {
            try {
                dialog?.dismiss()
            } catch (ignored: Exception) {
            }
        }
    }

    private fun updateTabVisibility() {
        for (i in 0..2) {
            tabsAdapter.isTabVisible(i, viewPager.currentItem == i)
        }
    }
}
