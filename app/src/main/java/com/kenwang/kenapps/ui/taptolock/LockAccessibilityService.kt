package com.kenwang.kenapps.ui.taptolock

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class LockAccessibilityService : AccessibilityService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_LOCK) {
            performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
        }
        return START_STICKY
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() = Unit

    companion object {
        const val ACTION_LOCK = "com.kenwang.kenapps.locktile.ACTION_LOCK"
    }
}
