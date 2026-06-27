package com.example.pawmesh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmesh.databinding.AiProfitLayoutBinding

class AiProfitActivity : AppCompatActivity() {

    private lateinit var binding: AiProfitLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AiProfitLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Intent로 전달받은 AI 결과 데이터 표시
        // val breed = intent.getStringExtra("breed")
        // val introText = intent.getStringExtra("introText")
        // val avatarImageUrl = intent.getStringExtra("avatarImageUrl")
        // val personalityTags = intent.getStringArrayListExtra("personalityTags")
    }
}