package com.example.bustrackingapp.feature_location_tracking.data.repository

import com.example.bustrackingapp.core.data.remote.dto.ApiResponse
import com.example.bustrackingapp.feature_location_tracking.data.remote.api.DriverLocationApiService
import com.example.bustrackingapp.feature_location_tracking.domain.model.DriverLocation
import com.example.bustrackingapp.feature_location_tracking.domain.repository.DriverLocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DriverLocationRepositoryImpl(
    private val api: DriverLocationApiService,
    private val dispatcher: CoroutineDispatcher
) : DriverLocationRepository {
    override suspend fun sendLocation(location: DriverLocation): ApiResponse<Unit> =
        withContext(dispatcher) { api.sendLocation(location) }

    override suspend fun getLocation(busId: String): ApiResponse<DriverLocation> =
        withContext(dispatcher) { api.getLocation(busId) }
}
