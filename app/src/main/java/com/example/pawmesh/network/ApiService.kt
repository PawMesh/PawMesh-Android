package com.example.pawmesh.network

import com.example.pawmesh.network.model.DogProfileResponse
import com.example.pawmesh.network.model.HumanProfileResponse
import com.example.pawmesh.network.model.LoginRequest
import com.example.pawmesh.network.model.LoginResponse
import com.example.pawmesh.network.model.NearbyDogResponse
import com.example.pawmesh.network.model.SendWalkRequestBody
import com.example.pawmesh.network.model.SignupRequest
import com.example.pawmesh.network.model.SignupResponse
import com.example.pawmesh.network.model.UpdateDogProfileRequest
import com.example.pawmesh.network.model.UpdateDogProfileResponse
import com.example.pawmesh.network.model.UpdateHumanProfileRequest
import com.example.pawmesh.network.model.UpdateHumanProfileResponse
import com.example.pawmesh.network.model.WalkRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // 로그인
    @POST("v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    // 회원가입
    @POST("v1/auth/signup/complete")
    suspend fun signup(
        @Body request: SignupRequest
    ): SignupResponse

    // 주변 강아지 목록
    @GET("v1/map-sessions/nearby")
    suspend fun getNearbyDogs(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("radiusKm") radiusKm: Double = 1.0
    ): NearbyDogResponse

    // 받은 산책 요청 목록
    @GET("v1/walk-requests/received")
    suspend fun getReceivedWalkRequests(
        @Query("receiverDogId") receiverDogId: Long
    ): List<WalkRequest>

    // 산책 요청 보내기
    @POST("v1/walk-requests")
    suspend fun sendWalkRequest(
        @Body request: SendWalkRequestBody
    ): WalkRequest

    // 산책 요청 수락
    @PATCH("v1/walk-requests/{id}/accept")
    suspend fun acceptWalkRequest(
        @Path("id") id: Long
    ): WalkRequest

    // 산책 요청 거절
    @PATCH("v1/walk-requests/{id}/reject")
    suspend fun rejectWalkRequest(
        @Path("id") id: Long
    ): WalkRequest

    // 강아지 프로필 조회
    @GET("v1/user/profile/dog")
    suspend fun getDogProfile(): DogProfileResponse

    // 보호자 프로필 조회
    @GET("v1/user/profile/human")
    suspend fun getHumanProfile(): HumanProfileResponse

    // 강아지 프로필 수정
    @PATCH("v1/user/profile/dog")
    suspend fun updateDogProfile(
        @Body request: UpdateDogProfileRequest
    ): UpdateDogProfileResponse

    // 보호자 프로필 수정
    @PATCH("v1/user/profile/human")
    suspend fun updateHumanProfile(
        @Body request: UpdateHumanProfileRequest
    ): UpdateHumanProfileResponse
}