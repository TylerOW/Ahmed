package com.example.bustrackingapp.feature_location_tracking.domain.model

/** Bus driver location payload */
data class DriverLocation(
    val busId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)
