package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmesh.databinding.StartLayoutBinding

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: 임시 - 항상 시작 화면 표시 (onboarding 완료 체크 비활성화)
        // val prefs = getSharedPreferences("pawmesh_prefs", Context.MODE_PRIVATE)
        // if (prefs.getBoolean("onboarding_done", false)) {
        //     startActivity(Intent(this, MainActivity::class.java))
        //     finish()
        //     return
        // }

        val binding = StartLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            startActivity(Intent(this, PictureActivity::class.java))
            finish()
        }
    }
}