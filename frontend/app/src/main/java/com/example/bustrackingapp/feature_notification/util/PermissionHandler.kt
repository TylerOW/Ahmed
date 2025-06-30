package com.example.bustrackingapp.feature_notification.util

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.core.app.ActivityCompat

/** Request POST_NOTIFICATIONS permission on Android 13+ */
object PermissionHandler {
    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }
}
