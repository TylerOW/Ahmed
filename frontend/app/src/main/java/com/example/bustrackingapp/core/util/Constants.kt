// app/src/main/java/com/example/bustrackingapp/core/util/Constants.kt
package com.example.bustrackingapp.core.util

import com.example.bustrackingapp.R

object Constants {

    /* ── Network ─────────────────────────────────────────────── */

    /** REST root (already includes `/api/`) */
    const val baseUrl = "https://alive-octopus-patient.ngrok-free.app/api/"

    /** Socket.IO connects to the server root (no /api) */
    const val socketBaseUrl = "https://alive-octopus-patient.ngrok-free.app"

    /* ── Storage ─────────────────────────────────────────────── */

    const val userPrefs = "user_prefs"

    /* ── UI Helpers ──────────────────────────────────────────── */

    val busColor = mapOf(
        "red"    to R.color.red,
        "green"  to R.color.green,
        "blue"   to R.color.blue,
        "orange" to R.color.orange,
        "other"  to R.color.black
    )

    val busIcon = mapOf(
        "red"    to R.drawable.bus_location_red,
        "green"  to R.drawable.bus_location_green,
        "blue"   to R.drawable.bus_location_blue,
        "orange" to R.drawable.bus_location_orange,
        "other"  to R.drawable.locate_bus
    )

    val busStatus = mapOf(
        "na"         to "NA",
        "in_route"   to "IN ROUTE",
        "delayed"    to "DELAYED",
        "cancelled"  to "CANCELLED",
        "break_down" to "BREAK DOWN"
    )

    object UserType {
        const val passenger = "passenger"
        const val driver    = "driver"
    }

    val days = listOf(
        "sunday",
        "monday",
        "tuesday",
        "wednesday",
        "thursday",
        "friday",
        "saturday"
    )
}
