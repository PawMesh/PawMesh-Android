package com.example.pawmesh.data.network.api

import com.example.pawmesh.data.network.dto.request.WalkRequestDto
import com.example.pawmesh.data.network.dto.response.WalkResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// walk-request-controller
interface WalkRequestApi {

    // [산책 요청 보내기]
    @POST("v1/walk-requests")
    suspend fun createWalkRequest(
        @Header("Authorization") token: String,
        @Body body: WalkRequestDto
    ): Response<WalkResponseDto>

    // [산책 요청 수락]
    @PATCH("v1/walk-requests/{id}/accept")
    suspend fun acceptWalkRequest(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<WalkResponseDto>

    // [산책 요청 취소]
    @PATCH("v1/walk-requests/{id}/cancel")
    suspend fun cancelWalkRequest(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<WalkResponseDto>

    // [산책 요청 거절]
    @PATCH("v1/walk-requests/{id}/reject")
    suspend fun rejectWalkRequest(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<WalkResponseDto>

    // [받은 산책 요청 목록 조회]
    @GET("v1/walk-requests/received")
    suspend fun getReceivedRequests(
        @Header("Authorization") token: String,
        @Query("receiverDogId") receiverDogId: Long
    ): Response<List<WalkResponseDto>>
}