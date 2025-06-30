package com.example.bustrackingapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BusTrackingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // ─── Create "bus_arrival_channel" notification channel ───
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "bus_arrival_channel",                     // <-- renamed here
                "Bus Arrival Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when a bus is near your stop"
            }
            getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }
        // ───────────────────────────────────────────────────────
    }
}
