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
        // 성공/실패 모두 AiProfitActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AiProfitActivity::class.java))
            finish()
        }, 2000)
    }
}