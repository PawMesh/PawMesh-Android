package com.example.pawmesh.data.network.api

import com.example.pawmesh.data.network.dto.request.AiPhotoRequestDto
import com.example.pawmesh.data.network.dto.request.LoginRequestDto
import com.example.pawmesh.data.network.dto.request.ReissueRequestDto
import com.example.pawmesh.data.network.dto.request.SignupCompleteRequestDto
import com.example.pawmesh.data.network.dto.response.AiPhotoJobDataDto
import com.example.pawmesh.data.network.dto.response.AiPhotoResultDataDto
import com.example.pawmesh.data.network.dto.response.NicknameCheckDataDto
import com.example.pawmesh.data.network.dto.response.ApiResponse
import com.example.pawmesh.data.network.dto.response.AuthTokenDto
import com.example.pawmesh.data.network.dto.response.PetImageUploadDataDto
import com.example.pawmesh.data.network.dto.response.SignupCompleteDataDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

// auth-command-controller + auth-query-controller
interface AuthApi {

    // [닉네임 중복 확인]
    @GET("v1/auth/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): Response<ApiResponse<NicknameCheckDataDto>>

    // [로그인]
    @POST("v1/auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): Response<ApiResponse<AuthTokenDto>>

    // [토큰 재발급]
    @POST("v1/auth/reissue")
    suspend fun reissue(
        @Body body: ReissueRequestDto
    ): Response<ApiResponse<AuthTokenDto>>

    // [회원가입 - 반려동물 사진 업로드]
    @Multipart
    @POST("v1/auth/signup/pet-images")
    suspend fun uploadPetImage(
        @Part image: MultipartBody.Part
    ): Response<ApiResponse<PetImageUploadDataDto>>

    // [회원가입 - AI 프로필 사진 생성 요청]
    @POST("v1/auth/signup/pet-images/ai-photos")
    suspend fun requestAiPhoto(
        @Header("X-Signup-Token") signupToken: String,
        @Body body: AiPhotoRequestDto
    ): Response<ApiResponse<AiPhotoJobDataDto>>

    // [회원가입 - AI 프로필 사진 생성 결과 조회]
    @GET("v1/auth/signup/pet-images/ai-photos/{jobId}")
    suspend fun getAiPhotoResult(
        @Header("X-Signup-Token") signupToken: String,
        @Path("jobId") jobId: String
    ): Response<ApiResponse<AiPhotoResultDataDto>>

    // [회원가입 완료]
    @POST("v1/auth/signup/complete")
    suspend fun signupComplete(
        @Header("X-Signup-Token") signupToken: String,
        @Body body: SignupCompleteRequestDto
    ): Response<ApiResponse<SignupCompleteDataDto>>
}