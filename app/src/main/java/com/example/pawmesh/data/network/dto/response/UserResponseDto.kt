package com.example.pawmesh.data.network.dto.response

data class DeleteUserDataDto(
    val userId: Int
)

data class UpdateDogProfileDataDto(
    val petId: Int
)

data class UpdateHumanProfileDataDto(
    val userId: Int
)

data class DogProfileDataDto(
    val petId: Int,
    val petName: String,
    val breed: String,
    val personalityTags: List<String>,
    val caution: String,
    val introText: String,
    val avatarImageUrl: String,
    val originalImageUrl: String
)

data class HumanProfileDataDto(
    val userId: Int,
    val nickname: String,
    val gender: String,
    val ageGroup: String,
    val walkStyles: List<String>,
    val matchGender: String,
    val matchAgeFrom: String,
    val matchAgeTo: String
)