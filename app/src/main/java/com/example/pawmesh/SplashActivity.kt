package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pawmesh.data.network.dto.request.AiPhotoRequestDto
import com.example.pawmesh.data.network.dto.response.AiPhotoResultDataDto
import com.example.pawmesh.data.network.retrofit.RetrofitClient
import com.example.pawmesh.databinding.PictureSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PictureSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signupToken = intent.getStringExtra("signup_token") ?: ""
        val petName = intent.getStringExtra("pet_name") ?: ""

        lifecycleScope.launch {
            requestAiPhotoAndNavigate(signupToken, petName)
        }
    }

    private suspend fun requestAiPhotoAndNavigate(signupToken: String, petName: String) {
        try {
            val jobResponse = RetrofitClient.authApi.requestAiPhoto(
                signupToken = signupToken,
                body = AiPhotoRequestDto(petName = petName)
            )
            Log.d("SplashActivity", "requestAiPhoto: HTTP ${jobResponse.code()}, body=${jobResponse.body()}, error=${jobResponse.errorBody()?.string()}")

            if (!jobResponse.isSuccessful || jobResponse.body()?.isSuccess != true) {
                showErrorAndFinish()
                return
            }

            val jobId = jobResponse.body()!!.data.jobId
            Log.d("SplashActivity", "jobId=$jobId, polling start")

            val result = pollUntilComplete(signupToken, jobId) ?: run {
                Log.e("SplashActivity", "polling timed out or all failed")
                showErrorAndFinish()
                return
            }

            val intent = Intent(this@SplashActivity, AiProfitActivity::class.java).apply {
                putExtra("breed", result.breed)
                putExtra("introText", result.introText)
                putExtra("avatarImageUrl", result.avatarImageUrl)
                putStringArrayListExtra("personalityTags", ArrayList(result.personalityTags))
            }
            startActivity(intent)
            finish()

        } catch (e: Exception) {
            Log.e("SplashActivity", "exception: ${e::class.simpleName} - ${e.message}", e)
            showErrorAndFinish()
        }
    }

    private suspend fun pollUntilComplete(signupToken: String, jobId: String): AiPhotoResultDataDto? {
        repeat(30) {
            delay(2000)
            try {
                val response = RetrofitClient.authApi.getAiPhotoResult(
                    signupToken = signupToken,
                    jobId = jobId
                )
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val data = response.body()!!.data
                    Log.d("SplashActivity", "poll status=${data.status}")
                    when (data.status) {
                        "COMPLETED" -> return data
                        "FAILED" -> return null
                    }
                } else {
                    Log.w("SplashActivity", "poll failed: HTTP ${response.code()}, error=${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SplashActivity", "poll exception: ${e.message}")
            }
        }
        return null
    }

    private fun showErrorAndFinish() {
        Toast.makeText(this, "AI 프로필 생성에 실패했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        finish()
    }
}