package com.example.pawmesh.network.model

data class HumanProfileResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val data: HumanProfile,
    val success: Boolean
)

data class HumanProfile(
    val userId: Int,
    val nickname: String,
    val gender: String,
    val ageGroup: String,
    val walkStyles: List<String>,
    val matchGender: String,
    val matchAgeFrom: String,
    val matchAgeTo: String
)

// 보호자 프로필 수정 요청
data class UpdateHumanProfileRequest(
    val gender: String,
    val ageGroup: String,
    val walkStyles: List<String>,
    val matchGender: String,
    val matchAgeFrom: String,
    val matchAgeTo: String
)

// 보호자 프로필 수정 응답
data class UpdateHumanProfileResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val data: UpdateHumanProfileData,
    val success: Boolean
)

data class UpdateHumanProfileData(
    val userId: Int
)