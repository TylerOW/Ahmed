package com.example.bustrackingapp.core.domain.models

data class Location(
    val _id: String? = null,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val coordinates: List<Double>? = null,
    val address: String? = null
)