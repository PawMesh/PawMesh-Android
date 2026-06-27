package com.example.pawmesh.data.network.dto.request

data class WalkRequestDto(
    val requesterDogId: Int,
    val receiverDogId: Int,
    val message: String
)