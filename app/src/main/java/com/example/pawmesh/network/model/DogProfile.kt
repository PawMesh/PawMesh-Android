package com.example.pawmesh.network.model

data class DogProfileResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val data: DogProfile,
    val success: Boolean
)

data class DogProfile(
    val petId: Int,
    val petName: String,
    val breed: String,
    val personalityTags: List<String>,
    val caution: String,
    val introText: String,
    val avatarImageUrl: String,
    val originalImageUrl: String
)

// 강아지 프로필 수정 요청
data class UpdateDogProfileRequest(
    val petName: String,
    val breed: String,
    val personalityTags: List<String>,
    val caution: String,
    val introText: String
)

// 강아지 프로필 수정 응답
data class UpdateDogProfileResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val data: UpdateDogProfileData,
    val success: Boolean
)

data class UpdateDogProfileData(
    val petId: Int
)