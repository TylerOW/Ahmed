package com.example.bustrackingapp.feature_bus_stop.data.api

import com.example.bustrackingapp.core.data.remote.dto.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/** API endpoints for user favorite stops */
interface FavoriteApiService {
    @GET("favorites")
    suspend fun getFavorites(): ApiResponse<List<String>>

    @POST("favorites")
    suspend fun addFavorite(@Body body: Map<String, String>): ApiResponse<Unit>

    @DELETE("favorites/{stopNo}")
    suspend fun removeFavorite(@Path("stopNo") stopNo: String): ApiResponse<Unit>
}
