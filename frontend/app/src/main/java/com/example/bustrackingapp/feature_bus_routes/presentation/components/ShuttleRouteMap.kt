package com.example.bustrackingapp.feature_bus_routes.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * Map showing the full UNITEN shuttle route using Google Maps Compose.
 * Polylines currently connect stops directly; replace with road-based
 * directions from backend/Google Directions API when available.
 */
@Composable
fun ShuttleRouteMap(modifier: Modifier = Modifier) {
    // Coordinates defining the fixed shuttle route in order
    val routePoints = remember {
        listOf(
            LatLng(2.976673, 101.734034), // ATM/Library
            LatLng(2.977944, 101.730570), // Admin Building
            LatLng(2.975777, 101.728832), // Murni Hostel
            LatLng(2.962569, 101.725598), // BW
            LatLng(2.965783, 101.731220), // Amanah Hostel
            LatLng(2.968112, 101.728183), // DSS
            LatLng(2.970936, 101.730657), // ILMU Hostel
            LatLng(2.975298, 101.729192), // COE
            LatLng(2.976673, 101.734034)  // ATM/Library
        )
    }
    // TODO fetch detailed polyline from backend Directions API so
    // that the route follows actual roads rather than straight lines

    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(Unit) {
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(routePoints.first(), 15f)
        )
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(),
        uiSettings = MapUiSettings(zoomControlsEnabled = true)
    ) {
        Polyline(points = routePoints, color = Color.Blue, width = 6f)

        // Marker for each bus stop on the route loop
        routePoints.dropLast(1).forEach { point ->
            Marker(state = MarkerState(position = point))
        }
    }
}
