package com.example.pawmesh.data.network.dto.request

data class SignupCompleteRequestDto(
    val nickname: String,
    val password: String,
    val gender: String,
    val ageGroup: String,
    val walkStyles: List<String>,
    val matchGender: String,
    val matchAgeFrom: String,
    val matchAgeTo: String,
    val petName: String,
    val breed: String,
    val personalityTags: List<String>,
    val caution: String,
    val introText: String
)