package com.example.pawmesh.network.model

data class NearbyDogResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val data: List<NearbyDog>,
    val success: Boolean
)

data class NearbyDog(
    val walkSessionId: Int,
    val dogId: Int,
    val characterImageUrl: String,
    val lat: Double,
    val lng: Double,
    val status: String,
    val tags: List<String>
)