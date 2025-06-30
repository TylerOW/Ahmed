package com.example.bustrackingapp.feature_location_tracking.domain.use_case

data class DriverLocationUseCases(
    val sendLocation: SendDriverLocationUseCase,
    val getLocation: GetDriverLocationUseCase
)
