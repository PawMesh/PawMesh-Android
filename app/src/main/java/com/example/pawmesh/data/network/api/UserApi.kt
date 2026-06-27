package com.example.pawmesh.data.network.api

import com.example.pawmesh.data.network.dto.request.UpdateDogProfileRequestDto
import com.example.pawmesh.data.network.dto.request.UpdateHumanProfileRequestDto
import com.example.pawmesh.data.network.dto.response.ApiResponse
import com.example.pawmesh.data.network.dto.response.DeleteUserDataDto
import com.example.pawmesh.data.network.dto.response.UpdateDogProfileDataDto
import com.example.pawmesh.data.network.dto.response.DogProfileDataDto
import com.example.pawmesh.data.network.dto.response.HumanProfileDataDto
import com.example.pawmesh.data.network.dto.response.UpdateHumanProfileDataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

// user-command-controller + user-query-controller
interface UserApi {

    // [회원 탈퇴]
    @DELETE("v1/user")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): Response<ApiResponse<DeleteUserDataDto>>

    // [반려동물 프로필 수정]
    @PATCH("v1/user/profile/dog")
    suspend fun updateDogProfile(
        @Header("Authorization") token: String,
        @Body body: UpdateDogProfileRequestDto
    ): Response<ApiResponse<UpdateDogProfileDataDto>>

    // [보호자 프로필 수정]
    @PATCH("v1/user/profile/human")
    suspend fun updateHumanProfile(
        @Header("Authorization") token: String,
        @Body body: UpdateHumanProfileRequestDto
    ): Response<ApiResponse<UpdateHumanProfileDataDto>>

    // [반려동물 프로필 조회]
    @GET("v1/user/profile/dog")
    suspend fun getDogProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<DogProfileDataDto>>

    // [보호자 프로필 조회]
    @GET("v1/user/profile/human")
    suspend fun getHumanProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<HumanProfileDataDto>>
}