package com.example.pawmesh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmesh.databinding.StartLayoutBinding

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("pawmesh_prefs", Context.MODE_PRIVATE)
        if (prefs.getBoolean("onboarding_done", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val binding = StartLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            startActivity(Intent(this, PictureActivity::class.java))
            finish()
        }
    }
}