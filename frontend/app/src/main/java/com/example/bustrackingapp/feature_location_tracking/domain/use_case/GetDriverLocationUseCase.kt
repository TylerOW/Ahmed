package com.example.bustrackingapp.feature_location_tracking.domain.use_case

import com.example.bustrackingapp.core.util.ApiHandler
import com.example.bustrackingapp.core.util.Resource
import com.example.bustrackingapp.feature_location_tracking.domain.model.DriverLocation
import com.example.bustrackingapp.feature_location_tracking.domain.repository.DriverLocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDriverLocationUseCase @Inject constructor(
    private val repo: DriverLocationRepository
) : ApiHandler() {
    operator fun invoke(busId: String): Flow<Resource<DriverLocation>> = makeRequest(
        apiCall = { repo.getLocation(busId) }
    )
}
