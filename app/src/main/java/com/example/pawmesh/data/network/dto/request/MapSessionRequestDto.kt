package com.example.pawmesh.data.network.dto.request

data class CreateMapSessionRequestDto(
    val dogId: Int,
    val currentLat: Double,
    val currentLng: Double
)

data class CompleteSessionRequestDto(
    val distanceM: Int,
    val durationSec: Int,
    val routePath: List<List<Double>>
)