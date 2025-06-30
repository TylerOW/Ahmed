package com.example.bustrackingapp.core.data.local

/** Coordinates for static bus stops used when drawing the default route. */
data class StaticBusStop(
    val stopNo: String,
    val name: String,
    val lat: Double,
    val lng: Double
)

val staticBusStops = listOf(
    StaticBusStop(
        stopNo = "UNITEN_C1",
        name = "College of Engineering",
        lat = 2.975298,
        lng = 101.729192
    ),
    StaticBusStop(
        stopNo = "UNITEN_M1",
        name = "Murni Hostel",
        lat = 2.975777,
        lng = 101.728832
    ),
    StaticBusStop(
        stopNo = "UNITEN_A1",
        name = "Administration Building",
        lat = 2.977944,
        lng = 101.730570
    ),
    StaticBusStop(
        stopNo = "UNITEN_L1",
        name = "Library",
        lat = 2.976673,
        lng = 101.734034
    ),
    StaticBusStop(
        stopNo = "UNITEN_I1",
        name = "ILMU Hostel",
        lat = 2.970936,
        lng = 101.730657
    ),
    StaticBusStop(
        stopNo = "UNITEN_DSS",
        name = "Dewan Seri Sarjana",
        lat = 2.968112,
        lng = 101.728183
    ),
    StaticBusStop(
        stopNo = "UNITEN_AM1",
        name = "Amanah Hostel",
        lat = 2.965783,
        lng = 101.731220
    ),
    StaticBusStop(
        stopNo = "UNITEN_BW",
        name = "College Of Information Technology",
        lat = 2.962569,
        lng = 101.725598
    )
)
