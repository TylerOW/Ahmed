package com.example.bustrackingapp.feature_location_tracking.data.remote.api

import com.example.bustrackingapp.core.data.remote.dto.ApiResponse
import com.example.bustrackingapp.feature_location_tracking.domain.model.DriverLocation
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DriverLocationApiService {
    @POST("locations/bus")
    suspend fun sendLocation(@Body location: DriverLocation): ApiResponse<Unit>

    @GET("locations/bus/{busId}")
    suspend fun getLocation(@Path("busId") busId: String): ApiResponse<DriverLocation>
}
