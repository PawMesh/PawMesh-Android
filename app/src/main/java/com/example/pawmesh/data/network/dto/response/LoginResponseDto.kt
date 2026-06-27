package com.example.pawmesh.data.network.dto.response

data class AuthTokenDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)

data class SignupCompleteDataDto(
    val userId: Int
)

data class PetImageUploadDataDto(
    val signupToken: String,
    val originalImageUrl: String
)

data class AiPhotoJobDataDto(
    val jobId: String
)

data class NicknameCheckDataDto(
    val available: Boolean
)

data class AiPhotoResultDataDto(
    val status: String,
    val avatarImageUrl: String,
    val breed: String,
    val personalityTags: List<String>,
    val introText: String
)