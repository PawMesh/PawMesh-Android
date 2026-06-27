package com.example.pawmesh.data.network.api

import com.example.pawmesh.data.network.dto.request.CompleteSessionRequestDto
import com.example.pawmesh.data.network.dto.request.CreateMapSessionRequestDto
import com.example.pawmesh.data.network.dto.request.LocationUpdateRequestDto
import com.example.pawmesh.data.network.dto.response.ApiResponse
import com.example.pawmesh.data.network.dto.response.CompleteSessionDataDto
import com.example.pawmesh.data.network.dto.response.LocationUpdateDataDto
import com.example.pawmesh.data.network.dto.response.NearbySessionDto
import com.example.pawmesh.data.network.dto.response.PartnerLocationDataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// map-session-controller
interface MapApi {

    // [산책 세션 생성]
    @POST("v1/map-sessions")
    suspend fun createMapSession(
        @Header("Authorization") token: String,
        @Body body: CreateMapSessionRequestDto
    ): Response<ApiResponse<LocationUpdateDataDto>>

    // [산책 세션 종료]
    @DELETE("v1/map-sessions/{sessionId}")
    suspend fun deleteMapSession(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: Long
    ): Response<ApiResponse<LocationUpdateDataDto>>

    // [내 위치 업데이트]
    @PATCH("v1/map-sessions/{sessionId}/location")
    suspend fun updateLocation(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: Long,
        @Body body: LocationUpdateRequestDto
    ): Response<ApiResponse<LocationUpdateDataDto>>

    // [산책 완료]
    @PATCH("v1/map-sessions/{sessionId}/complete")
    suspend fun completeSession(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: Long,
        @Body body: CompleteSessionRequestDto
    ): Response<ApiResponse<CompleteSessionDataDto>>

    // [파트너 위치 조회]
    @GET("v1/map-sessions/{sessionId}/partner-location")
    suspend fun getPartnerLocation(
        @Header("Authorization") token: String,
        @Path("sessionId") sessionId: Long
    ): Response<ApiResponse<PartnerLocationDataDto>>

    // [주변 산책 세션 조회]
    @GET("v1/map-sessions/nearby")
    suspend fun getNearbySessions(
        @Header("Authorization") token: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("radiusKm") radiusKm: Double = 1.0
    ): Response<ApiResponse<List<NearbySessionDto>>>
}