package com.example.pawmesh.data.network.dto.response

data class WalkResponseDto(
    val id: Int,
    val requesterDogId: Int,
    val receiverDogId: Int,
    val message: String,
    val status: String,
    val walkSessionId: Int?,
    val createdAt: String,
    val respondedAt: String?
)