package com.example.bustrackingapp.feature_home.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bustrackingapp.ui.theme.Red400
import com.example.bustrackingapp.ui.theme.White
import com.example.bustrackingapp.core.data.local.staticBusStops
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    snackbarState: SnackbarHostState = remember { SnackbarHostState() },
    onNearbyBusClick: (String) -> Unit,
    onNearbyBusStopClick: (String) -> Unit,
    onAllBusStopsClick: () -> Unit,
    userType: String
) {
    LaunchedEffect(
        homeViewModel.uiState.errorLocation,
        homeViewModel.uiState.errorNearbyBuses,
        homeViewModel.uiState.errorNearbyStops
    ) {
        when {
            homeViewModel.uiState.errorLocation != null ->
                snackbarState.showSnackbar(homeViewModel.uiState.errorLocation!!)
            homeViewModel.uiState.errorNearbyBuses != null ->
                snackbarState.showSnackbar(homeViewModel.uiState.errorNearbyBuses!!)
            homeViewModel.uiState.errorNearbyStops != null ->
                snackbarState.showSnackbar(homeViewModel.uiState.errorNearbyStops!!)
        }
    }

    LaunchedEffect(userType) {
        homeViewModel.startBusLocationUpdates("bus1")
        if (userType == com.example.bustrackingapp.core.util.Constants.UserType.driver) {
            homeViewModel.startDriverTracking()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (userType == com.example.bustrackingapp.core.util.Constants.UserType.driver)
                            "Bus Driver" else "Bus Tracker",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (
                        homeViewModel.uiState.errorNearbyBuses != null ||
                        homeViewModel.uiState.errorNearbyStops != null
                    ) Red400 else MaterialTheme.colorScheme.surface,
                    contentColor = if (
                        homeViewModel.uiState.errorNearbyBuses != null ||
                        homeViewModel.uiState.errorNearbyStops != null
                    ) White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column {
                Text(
                    text = "Nearby Buses",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                homeViewModel.uiState.notificationMessage?.let { msg ->
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White
                    )
                }

                NearbyBusesList(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    buses = { homeViewModel.uiState.nearbyBuses },
                    isLoading = homeViewModel.uiState.isLoadingLocation ||
                            homeViewModel.uiState.isLoadingNearbyBuses,
                    onRefresh = homeViewModel::getNearbyBuses,
                    onBusClick = onNearbyBusClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nearby Bus Stops", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "All Bus Stops",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onAllBusStopsClick() }
                            .padding(4.dp)
                    )
                }

                NearbyBusStopsList(
                    modifier = Modifier.weight(3f),
                    busStops = { homeViewModel.uiState.nearbyBusStops },
                    isLoading = homeViewModel.uiState.isLoadingLocation ||
                            homeViewModel.uiState.isLoadingNearbyStops,
                    isRefreshing = homeViewModel.uiState.isRefreshingNearbyStops,
                    onRefresh = homeViewModel::getNearbyStops,
                    onBusStopClick = onNearbyBusStopClick
                )

                Spacer(modifier = Modifier.height(12.dp))

                val defaultCenter = remember {
                    val avgLat = staticBusStops.map { it.lat }.average()
                    val avgLng = staticBusStops.map { it.lng }.average()
                    LatLng(avgLat, avgLng)
                }

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        homeViewModel.uiState.driverLocation ?: defaultCenter,
                        15f
                    )
                }

                LaunchedEffect(Unit) {
                    snapshotFlow { homeViewModel.uiState.driverLocation }
                        .filterNotNull()
                        .distinctUntilChanged()
                        .debounce(1000)
                        .collect { loc ->
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(loc, 15f)
                            )
                        }
                }

                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = userType ==
                                com.example.bustrackingapp.core.util.Constants.UserType.driver
                    ),
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    val driverLoc = homeViewModel.uiState.driverLocation
                    if (driverLoc != null) {
                        Marker(
                            state = MarkerState(position = driverLoc),
                            title = "Bus Location",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
