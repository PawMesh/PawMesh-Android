package com.example.pawmesh

import android.content.Intent
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

class AiProfitActivity : AppCompatActivity() {

    private val tag = "PawMesh_AiProfit"
    private lateinit var binding: AiProfitLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AiProfitLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val avatarImageUrl = intent.getStringExtra("avatar_image_url")
        val breed = intent.getStringExtra("breed")
        val introText = intent.getStringExtra("intro_text")

        Log.d(tag, "받은 avatarImageUrl=$avatarImageUrl")
        Log.d(tag, "받은 breed=$breed")
        Log.d(tag, "받은 introText=$introText")

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
                    e?.logRootCauses(tag)
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

        breed?.let { binding.etBreed.setText(it) }
        introText?.let { binding.tvAiIntro.text = it }

        // 완벽해요 → 보호자 프로필 화면
        binding.btnSelect1.setOnClickListener {
            startActivity(Intent(this, GuardianProfitActivity::class.java))
            finish()
        }

        // 사진 다시 선택하기 → 뒤로
        binding.btnSelect2.setOnClickListener {
            finish()
        }
    }
}