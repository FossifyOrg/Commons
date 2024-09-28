package org.fossify.commons

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ProcessLifecycleOwner
import org.fossify.commons.extensions.appLockManager
import org.fossify.commons.extensions.checkUseEnglish
import org.fossify.commons.extensions.syncGlobalConfig
import org.fossify.commons.helpers.MyContentProvider
import org.fossify.commons.helpers.isTiramisuPlus

open class FossifyApp : Application() {

    open val isAppLockFeatureAvailable = false

    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
        setupAppLockManager()
        setupGlobalReceiver()
    }

    private fun setupAppLockManager() {
        if (isAppLockFeatureAvailable) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(appLockManager)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun setupGlobalReceiver() {
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val result = goAsync()
                context.syncGlobalConfig {
                    result.finish()
                }
            }
        }

        if (isTiramisuPlus()) {
            registerReceiver(
                broadcastReceiver,
                IntentFilter(MyContentProvider.GLOBAL_CONFIG_UPDATED),
                RECEIVER_EXPORTED
            )
        } else {
            registerReceiver(
                broadcastReceiver,
                IntentFilter(MyContentProvider.GLOBAL_CONFIG_UPDATED)
            )
        }
    }
}
