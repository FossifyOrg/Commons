package org.fossify.commons.helpers

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.fossify.commons.compose.extensions.config

class AppLockManager(
    private val context: Application,
    private val config: BaseConfig = context.config
) : DefaultLifecycleObserver {

    @Volatile
    private var isLocked: Boolean = config.isAppPasswordProtectionOn

    @Volatile
    private var shouldUpdateLockState: Boolean = true

    fun lock() {
        isLocked = true
        resetUnlockTimestamp(0L)
    }

    fun unlock() {
        isLocked = false
        resetUnlockTimestamp()
    }

    fun isLocked(): Boolean {
        updateLockState()
        return isLocked
    }

    override fun onResume(owner: LifecycleOwner) = updateLockState()

    override fun onStop(owner: LifecycleOwner) {
        shouldUpdateLockState = true
        if (!isLocked) {
            resetUnlockTimestamp()
        }
    }

    private fun updateLockState() {
        if (config.isAppPasswordProtectionOn) {
            val now = System.currentTimeMillis()
            val unlockExpired = now - config.lastUnlockTimestampMs > config.unlockTimeoutDurationMs
            if (unlockExpired && shouldUpdateLockState) {
                isLocked = true
                shouldUpdateLockState = false
            } else {
                resetUnlockTimestamp(now)
            }
        } else {
            isLocked = false
        }
    }

    private fun resetUnlockTimestamp(timestamp: Long = System.currentTimeMillis()) {
        config.lastUnlockTimestampMs = timestamp
    }

    companion object {
        @Volatile
        private var instance: AppLockManager? = null

        fun getInstance(appContext: Application): AppLockManager {
            return instance ?: synchronized(this) {
                instance ?: AppLockManager(appContext).also { instance = it }
            }
        }
    }
}

