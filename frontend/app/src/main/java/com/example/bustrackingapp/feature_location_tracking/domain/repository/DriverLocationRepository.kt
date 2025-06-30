package com.example.bustrackingapp.feature_location_tracking.domain.repository

import com.example.bustrackingapp.core.data.remote.dto.ApiResponse
import com.example.bustrackingapp.feature_location_tracking.domain.model.DriverLocation

interface DriverLocationRepository {
    suspend fun sendLocation(location: DriverLocation): ApiResponse<Unit>
    suspend fun getLocation(busId: String): ApiResponse<DriverLocation>
}
