package com.example.pawmesh

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.pawmesh.databinding.AiProfitLayoutBinding
import com.google.android.material.chip.Chip

class AiProfitActivity : AppCompatActivity() {

    private lateinit var binding: AiProfitLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AiProfitLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val breed = intent.getStringExtra("breed") ?: ""
        val introText = intent.getStringExtra("introText") ?: ""
        val avatarImageUrl = intent.getStringExtra("avatarImageUrl") ?: ""
        val personalityTags = intent.getStringArrayListExtra("personalityTags") ?: arrayListOf()

        binding.textView8.text = "${breed}를 소개합니다!"
        binding.etBreed.setText(breed)
        binding.tvAiIntro.text = introText

        if (avatarImageUrl.isNotEmpty()) {
            binding.imageView4.load(avatarImageUrl)
        }

        binding.chipGroupPersonality.removeAllViews()
        personalityTags.forEach { tag ->
            val chip = Chip(this).apply {
                text = tag
                isClickable = true
                isCheckable = true
                isChecked = true
                chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#EFF6FF"))
                setTextColor(Color.parseColor("#3D6FF0"))
            }
            binding.chipGroupPersonality.addView(chip)
        }

        binding.btnSelect1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnSelect2.setOnClickListener {
            finish()
        }
    }
}