package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmesh.databinding.PictureSplashBinding

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PictureSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: API 응답 완료 후 이동하도록 변경 필요 (현재는 2초 딜레이)
        Handler(Looper.getMainLooper()).postDelayed({
            getSharedPreferences("pawmesh_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("onboarding_done", true)
                .apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}