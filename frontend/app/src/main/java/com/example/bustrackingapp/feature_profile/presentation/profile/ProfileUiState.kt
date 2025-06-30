package com.example.bustrackingapp.feature_profile.presentation.profile

import com.example.bustrackingapp.core.domain.models.User

data class ProfileUiState(
    val token: String = "",
    val user: User? = null,
    val favoriteStops: List<com.example.bustrackingapp.feature_bus_stop.domain.model.BusStopWithRoutes> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
