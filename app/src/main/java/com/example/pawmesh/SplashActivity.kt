package com.example.pawmesh

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pawmesh.data.network.dto.request.AiPhotoRequestDto
import com.example.pawmesh.data.network.dto.response.AiPhotoResultDataDto
import com.example.pawmesh.data.network.retrofit.RetrofitClient
import com.example.pawmesh.databinding.PictureSplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class SplashActivity : AppCompatActivity() {

    private val TAG = "PawMesh_Photo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PictureSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUri = intent.getStringExtra("photo_uri")?.let { Uri.parse(it) }
        val dogName = intent.getStringExtra("dog_name") ?: ""

        if (photoUri == null) {
            Log.e(TAG, "3. SplashActivity - photo_uri 없음")
            showErrorAndFinish()
            return
        }

        Log.d(TAG, "3. SplashActivity 시작 | uri=$photoUri, dogName=$dogName")

        lifecycleScope.launch {
            try {
                Log.d(TAG, "3-1. 이미지 업로드 시작")
                val imagePart = withContext(Dispatchers.IO) { uriToMultipart(photoUri) }

                if (imagePart == null) {
                    Log.e(TAG, "3-1. 이미지 변환 실패")
                    showErrorAndFinish()
                    return@launch
                }

                val uploadResponse = RetrofitClient.authApi.uploadPetImage(imagePart)
                Log.d(TAG, "3-1. 업로드 응답 코드: ${uploadResponse.code()}, body: ${uploadResponse.body()}")

                val signupToken = uploadResponse.body()?.data?.signupToken ?: run {
                    Log.e(TAG, "3-1. signupToken 없음")
                    showErrorAndFinish()
                    return@launch
                }

                Log.d(TAG, "3-2. AI 생성 요청 | signupToken=$signupToken, petName=$dogName")
                val aiResponse = RetrofitClient.authApi.requestAiPhoto(
                    signupToken = signupToken,
                    body = AiPhotoRequestDto(petName = dogName)
                )
                Log.d(TAG, "3-2. AI 요청 응답 코드: ${aiResponse.code()}, body: ${aiResponse.body()}")

                val jobId = aiResponse.body()?.data?.jobId ?: run {
                    Log.e(TAG, "3-2. jobId 없음")
                    showErrorAndFinish()
                    return@launch
                }

                Log.d(TAG, "3-3. 폴링 시작 | jobId=$jobId")
                val result = pollUntilComplete(signupToken, jobId)

                if (result != null) {
                    navigateToAiProfit(result)
                } else {
                    showErrorAndFinish()
                }

            } catch (e: Exception) {
                Log.e(TAG, "3. 예외 발생: ${e.javaClass.simpleName} - ${e.message}")
                showErrorAndFinish()
            }
        }
    }

    private suspend fun pollUntilComplete(signupToken: String, jobId: String): AiPhotoResultDataDto? {
        repeat(200) { attempt ->
            delay(3000)
            val resultResponse = RetrofitClient.authApi.getAiPhotoResult(signupToken, jobId)
            val result = resultResponse.body()?.data
            Log.d(TAG, "3-3. 폴링 ${attempt + 1}회 | status=${result?.status}")

            when (result?.status) {
                "DONE" -> {
                    Log.d(TAG, "3-3. AI 생성 완료! avatarImageUrl=${result.avatarImageUrl}")
                    return result
                }
                "FAILED" -> {
                    Log.e(TAG, "3-3. AI 생성 실패")
                    return null
                }
            }
        }
        Log.w(TAG, "3-3. 폴링 타임아웃")
        return null
    }

    private fun navigateToAiProfit(result: AiPhotoResultDataDto) {
        val intent = Intent(this, AiProfitActivity::class.java).apply {
            putExtra("avatar_image_url", result.avatarImageUrl)
            putExtra("breed", result.breed)
            putExtra("intro_text", result.introText)
            putStringArrayListExtra("personality_tags", ArrayList(result.personalityTags))
        }
        startActivity(intent)
        finish()
    }

    private fun uriToMultipart(uri: Uri): MultipartBody.Part? {
        return try {
            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val bytes = contentResolver.openInputStream(uri)?.readBytes() ?: return null
            val requestBody = bytes.toRequestBody(mimeType.toMediaType())
            MultipartBody.Part.createFormData("image", "photo.jpg", requestBody)
        } catch (e: Exception) {
            Log.e(TAG, "uriToMultipart 실패: ${e.message}")
            null
        }
    }

    private fun showErrorAndFinish() {
        Toast.makeText(this, "AI 프로필 생성에 실패했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        finish()
    }
}
