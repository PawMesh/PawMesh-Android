package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmesh.databinding.GuardianProfitLayoutBinding

class GuardianProfitActivity : AppCompatActivity() {

    private lateinit var binding: GuardianProfitLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GuardianProfitLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnComplete.setOnClickListener {
            getSharedPreferences("pawmesh_prefs", MODE_PRIVATE)
                .edit()
                .putBoolean("onboarding_done", true)
                .apply()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}