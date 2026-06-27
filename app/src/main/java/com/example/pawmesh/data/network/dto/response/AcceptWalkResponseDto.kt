package com.example.pawmesh.data.network.dto.response

data class AcceptWalkResponseDto(
    val request: AcceptedRequestDto,
    val walkSession: WalkSessionDto
)

data class AcceptedRequestDto(
    val id: Int,
    val status: String,
    val walkSessionId: Int,
    val respondedAt: String
)

data class WalkSessionDto(
    val id: Int,
    val dogId: Int,
    val partnerDogId: Int,
    val status: String,
    val startedAt: String?
)