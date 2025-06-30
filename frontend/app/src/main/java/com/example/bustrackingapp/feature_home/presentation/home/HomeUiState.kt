package com.example.bustrackingapp.feature_home.presentation.home

import android.location.Location
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.bustrackingapp.feature_bus.domain.models.BusWithRoute
import com.example.bustrackingapp.feature_bus_stop.domain.model.BusStopWithRoutes
import com.example.bustrackingapp.core.data.local.staticBusStops


data class HomeUiState(
    val nearbyBusStops       : List<BusStopWithRoutes>    = emptyList(),
    val nearbyBuses          : List<BusWithRoute>         = emptyList(),
    val location             : Location?                  = null,
    val isLoadingNearbyStops : Boolean                    = false,
    val isLoadingNearbyBuses : Boolean                    = false,
    val isLoadingLocation    : Boolean                    = false,
    val isRefreshingNearbyStops  : Boolean               = false,
    val isRefreshingNearbyBuses  : Boolean               = false,
    val errorNearbyBuses     : String?                    = null,
    val errorNearbyStops     : String?                    = null,
    val closestStopName      : String?                    = null,
    val errorLocation        : String?                    = null,

    /** NEW: message shown under "Nearby Buses" when a bus is approaching */
    val notificationMessage  : String?                    = null,

    /** Latest bus driver location */
    val driverLocation      : com.google.android.gms.maps.model.LatLng? = null,

    /** Static route points for drawing the map polyline */
    val routePoints : List<Pair<Double, Double>> =
        staticBusStops.map { it.lat to it.lng }
)

//@Stable
//interface HomeUiState {
//    val nearbyBusStops : List<BusStopWithRoutes>
//    val nearbyBuses : List<BusWithRoute>
//    val isLoadingNearbyStops : Boolean
//    val isLoadingNearbyBuses : Boolean
//    val isRefreshingNearbyStops : Boolean
//    val isRefreshingNearbyBuses : Boolean
//    val errorNearbyBuses : String?
//    val errorNearbyStops : String?
//}
//
//class MutableHomeUiState: HomeUiState {
//    override var nearbyBusStops : List<BusStopWithRoutes> by mutableStateOf(emptyList())
//    override var nearbyBuses : List<BusWithRoute> by mutableStateOf(emptyList())
//    override var isLoadingNearbyStops : Boolean by mutableStateOf(false)
//    override var isLoadingNearbyBuses : Boolean by mutableStateOf(false)
//    override var isRefreshingNearbyStops : Boolean by mutableStateOf(false)
//    override var isRefreshingNearbyBuses : Boolean by mutableStateOf(false)
//    override var errorNearbyBuses : String? by mutableStateOf(null)
//    override var errorNearbyStops : String? by mutableStateOf(null)
//}

val routePoints : List<Pair<Double,Double>> =
    staticBusStops.map { it.lat to it.lng }

