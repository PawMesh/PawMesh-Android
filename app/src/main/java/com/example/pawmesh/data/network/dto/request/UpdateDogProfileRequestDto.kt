package com.example.pawmesh.data.network.dto.request

data class UpdateDogProfileRequestDto(
    val petName: String,
    val breed: String,
    val personalityTags: List<String>,
    val caution: String,
    val introText: String
)