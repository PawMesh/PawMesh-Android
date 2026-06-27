package com.example.pawmesh.network.model

// 받은 산책 요청
data class WalkRequest(
    val id: Int,
    val requesterDogId: Int,
    val receiverDogId: Int,
    val message: String,
    val status: String,
    val walkSessionId: Int,
    val createdAt: String,
    val respondedAt: String
)

// 산책 요청 보내기 Request
data class SendWalkRequestBody(
    val requesterDogId: Int,
    val receiverDogId: Int,
    val message: String
)