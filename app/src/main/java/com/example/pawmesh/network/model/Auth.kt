package com.example.pawmesh.network.model

// 로그인 요청
data class LoginRequest(
    val nickname: String,
    val password: String
)

// 로그인 응답
data class LoginResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val data: LoginData,
    val success: Boolean
)

data class LoginData(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)

// 회원가입 요청
data class SignupRequest(
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

// 회원가입 응답
data class SignupResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val data: SignupData,
    val success: Boolean
)

data class SignupData(
    val userId: Int
)