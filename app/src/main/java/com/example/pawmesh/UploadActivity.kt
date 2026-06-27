package com.example.pawmesh

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pawmesh.databinding.UplodeLayoutBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: UplodeLayoutBinding

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { showPhoto(it) }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) galleryLauncher.launch("image/*")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UplodeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // PictureActivity에서 전달받은 사진 표시
        intent.getStringExtra("photo_uri")?.let {
            showPhoto(Uri.parse(it))
        }

        // 사진 다시 선택하기
        binding.btnSelect2.setOnClickListener {
            openGallery()
        }

        // AI 프로필 생성하기 버튼 → 로딩 스플래시로 이동
        binding.btnSelect1.setOnClickListener {
            // TODO: AI API POST 구현 필요
            // - 선택한 사진(Uri)과 강아지 이름을 서버에 multipart/form-data로 전송
            // - API 호출 후 SplashActivity에서 응답 대기 및 MainActivity로 이동
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }
    }

    private fun showPhoto(uri: Uri) {
        binding.imageView2.setImageURI(uri)
    }

    private fun openGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            galleryLauncher.launch("image/*")
        } else {
            permissionLauncher.launch(permission)
        }
    }
}
