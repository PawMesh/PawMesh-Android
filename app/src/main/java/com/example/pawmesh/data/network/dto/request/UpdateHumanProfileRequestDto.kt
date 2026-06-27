package com.example.pawmesh.data.network.dto.request

data class UpdateHumanProfileRequestDto(
    val gender: String,
    val ageGroup: String,
    val walkStyles: List<String>,
    val matchGender: String,
    val matchAgeFrom: String,
    val matchAgeTo: String
)