package com.kenwang.kenapps.ui.taptolock

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.Icon
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import com.kenwang.kenapps.R

class LockTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        qsTile?.apply {
            state = Tile.STATE_INACTIVE
            label = getString(R.string.lock_tile_label)
            contentDescription = "點一下鎖定螢幕"
            icon = Icon.createWithResource(this@LockTileService, android.R.drawable.ic_lock_lock)
            updateTile()
        }
    }

    override fun onClick() {
        super.onClick()

        // 檢查 Accessibility Service 是否已啟用
        if (!isAccessibilityServiceEnabled()) {
            Toast.makeText(this, "請先到「設定 → 無障礙」開啟鎖定服務", Toast.LENGTH_LONG).show()
            // 開啟無障礙設定頁
            val settingsIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                settingsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            startActivityAndCollapse(pendingIntent)
            return
        }

        // 發送鎖定指令
        val lockIntent = Intent(this, LockAccessibilityService::class.java).apply {
            action = LockAccessibilityService.ACTION_LOCK
        }
        startService(lockIntent)
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityManager =
            getSystemService(ACCESSIBILITY_SERVICE) as android.view.accessibility.AccessibilityManager

        val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
            android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC
        )

        val expectedComponent = ComponentName(this, LockAccessibilityService::class.java)

        return enabledServices.any { serviceInfo ->
            val enabledComponent = ComponentName(
                serviceInfo.resolveInfo.serviceInfo.packageName,
                serviceInfo.resolveInfo.serviceInfo.name
            )
            enabledComponent == expectedComponent
        }
    }
}
