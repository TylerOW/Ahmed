package com.example.bustrackingapp.feature_notification.domain

import com.google.android.gms.maps.model.LatLng

/** Basic bus stop model with favorite flag */
data class BusStop(
    val stopNo: String,
    val name: String,
    val location: LatLng,
    val isFavorited: Boolean = false
)
