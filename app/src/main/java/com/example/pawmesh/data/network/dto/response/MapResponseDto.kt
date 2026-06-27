package com.example.pawmesh.data.network.dto.response

data class LocationUpdateDataDto(
    val walkSessionId: Int?
)

data class CompleteSessionDataDto(
    val walkSessionId: Int?,
    val distanceM: Int?,
    val durationSec: Int?
)

data class PartnerLocationDataDto(
    val partnerDogId: Int?,
    val characterImageUrl: String?,
    val lat: Double?,
    val lng: Double?
)

data class NearbySessionDto(
    val walkSessionId: Int?,
    val dogId: Int?,
    val characterImageUrl: String?,
    val lat: Double?,
    val lng: Double?,
    val status: String?,
    val tags: List<String>? = emptyList()
)