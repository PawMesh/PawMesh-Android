package com.example.pawmesh

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pawmesh.databinding.AiProfitLayoutBinding
import com.google.android.material.chip.Chip

class AiProfitActivity : AppCompatActivity() {

    private val tag = "PawMesh_AiProfit"
    private lateinit var binding: AiProfitLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AiProfitLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val avatarImageUrl = intent.getStringExtra("avatar_image_url")
        val breed = intent.getStringExtra("breed") ?: ""
        val introText = intent.getStringExtra("intro_text") ?: ""
        val personalityTags = intent.getStringArrayListExtra("personality_tags") ?: arrayListOf()

        Log.d(tag, "받은 avatarImageUrl=$avatarImageUrl, breed=$breed")

        Glide.with(this)
            .load(avatarImageUrl)
            .circleCrop()
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(tag, "Glide 로드 실패 | url=$model | 원인=${e?.message}")
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(tag, "Glide 로드 성공 | url=$model")
                    return false
                }
            })
            .into(binding.imageView4)

        binding.textView8.text = "${breed}를 소개합니다!"
        binding.etBreed.setText(breed)
        binding.tvAiIntro.text = introText

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
            startActivity(Intent(this, GuardianProfitActivity::class.java))
            finish()
        }

        binding.btnSelect2.setOnClickListener {
            finish()
        }
    }
}
