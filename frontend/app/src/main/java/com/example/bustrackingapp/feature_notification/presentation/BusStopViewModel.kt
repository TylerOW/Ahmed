package com.example.bustrackingapp.feature_notification.presentation

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bustrackingapp.core.util.Resource
import com.example.bustrackingapp.feature_bus_stop.domain.use_case.BusStopUseCases
import com.example.bustrackingapp.feature_notification.domain.BusStop
import com.example.bustrackingapp.feature_notification.util.NotificationHelper
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ViewModel that observes favorited stops and triggers notifications */
@HiltViewModel
class BusStopViewModel @Inject constructor(
    application: Application,
    private val busStopUseCases: BusStopUseCases
) : AndroidViewModel(application) {

    private val notificationHelper = NotificationHelper(application)

    private val _stops = MutableStateFlow<List<BusStop>>(emptyList())
    val stops: StateFlow<List<BusStop>> = _stops

    private val favoriteStops = busStopUseCases.toggleFavorite
        .favorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    init {
        loadStops()
    }

    private fun loadStops() {
        viewModelScope.launch {
            busStopUseCases.getAllBusStops().collect { res ->
                if (res is Resource.Success) {
                    val favs = favoriteStops.value
                    _stops.value = res.data?.map {
                        BusStop(
                            stopNo = it.stopNo,
                            name = it.name,
                            location = LatLng(it.location.lat, it.location.lng),
                            isFavorited = favs.contains(it.stopNo)
                        )
                    } ?: emptyList()
                }
            }
        }
    }

    fun toggleFavorite(stopNo: String) {
        viewModelScope.launch {
            busStopUseCases.toggleFavorite(stopNo)
        }
    }

    /** Called with the latest bus location; notifies when close to favorites */
    fun onBusLocationUpdate(busLocation: LatLng) {
        val favorites = favoriteStops.value
        _stops.value.filter { favorites.contains(it.stopNo) }.forEach { stop ->
            if (isBus5MinutesAway(busLocation, stop.location)) {
                notificationHelper.sendNotification(stop.name)
            }
        }
    }

    // REPLACE WITH BACKEND LATER
    fun simulateBusArrival() {
        val simulatedBus = LatLng(2.977944, 101.730570)
        onBusLocationUpdate(simulatedBus)
    }

    private fun isBus5MinutesAway(bus: LatLng, stop: LatLng): Boolean {
        val results = FloatArray(1)
        Location.distanceBetween(
            bus.latitude, bus.longitude,
            stop.latitude, stop.longitude,
            results
        )
        val speedMps = 8.33f // assume 30km/h
        val etaSeconds = results[0] / speedMps
        return etaSeconds <= 5 * 60
    }
}
