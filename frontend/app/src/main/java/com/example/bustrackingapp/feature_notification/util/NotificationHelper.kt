package com.example.bustrackingapp.feature_notification.util

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.bustrackingapp.R

/** Helper to build and dispatch local notifications */
class NotificationHelper(private val context: Context) {
    private val manager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification(stopName: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.locate_bus)
            .setContentTitle("Bus is arriving soon!")
            .setContentText("Your favorited stop ($stopName) is 5 minutes away.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(stopName.hashCode(), notification)
    }

    companion object {
        const val CHANNEL_ID = "bus_arrival_channel"
    }
}
