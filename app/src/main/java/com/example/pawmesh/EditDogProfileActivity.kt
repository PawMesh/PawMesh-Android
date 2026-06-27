package com.example.pawmesh

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pawmesh.data.local.TokenManager
import com.example.pawmesh.data.network.dto.request.UpdateDogProfileRequestDto
import com.example.pawmesh.data.network.retrofit.RetrofitClient
import com.example.pawmesh.databinding.ActivityEditDogProfileBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class EditDogProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDogProfileBinding
    private val personalityTags = mutableListOf<String>()
    private var introText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDogProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnAddTag.setOnClickListener {
            val tag = binding.etAddTag.text.toString().trim()
            if (tag.isNotEmpty() && !personalityTags.contains(tag)) {
                personalityTags.add(tag)
                binding.etAddTag.text.clear()
                refreshChips()
            }
        }

        binding.btnSave.setOnClickListener { saveProfile() }

        loadProfile()
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            val token = "Bearer ${TokenManager.getAccessToken()}"
            runCatching { RetrofitClient.userApi.getDogProfile(token) }
                .onSuccess { response ->
                    val data = response.body()?.data ?: return@onSuccess
                    binding.etPetName.setText(data.petName ?: "")
                    binding.etBreed.setText(data.breed ?: "")
                    binding.etCaution.setText(data.caution ?: "")
                    introText = data.introText ?: ""
                    personalityTags.clear()
                    personalityTags.addAll(data.personalityTags ?: emptyList())
                    refreshChips()
                }
                .onFailure {
                    Toast.makeText(this@EditDogProfileActivity, "프로필을 불러오지 못했어요.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun refreshChips() {
        binding.chipGroupPersonality.removeAllViews()
        personalityTags.toList().forEach { tag ->
            val chip = Chip(this).apply {
                text = tag
                isCloseIconVisible = true
                chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#EFF6FF"))
                setTextColor(Color.parseColor("#3D6FF0"))
                setOnCloseIconClickListener {
                    personalityTags.remove(tag)
                    refreshChips()
                }
            }
            binding.chipGroupPersonality.addView(chip)
        }
    }

    private fun saveProfile() {
        val petName = binding.etPetName.text.toString().trim()
        val breed = binding.etBreed.text.toString().trim()
        val caution = binding.etCaution.text.toString().trim()

        if (petName.isEmpty() || breed.isEmpty()) {
            Toast.makeText(this, "이름과 견종을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val token = "Bearer ${TokenManager.getAccessToken()}"
            val body = UpdateDogProfileRequestDto(
                petName = petName,
                breed = breed,
                personalityTags = personalityTags.toList(),
                caution = caution,
                introText = introText
            )
            runCatching { RetrofitClient.userApi.updateDogProfile(token, body) }
                .onSuccess { response ->
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditDogProfileActivity, "프로필이 저장되었어요.", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this@EditDogProfileActivity, "저장에 실패했어요. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                .onFailure {
                    Toast.makeText(this@EditDogProfileActivity, "네트워크 오류가 발생했어요.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
