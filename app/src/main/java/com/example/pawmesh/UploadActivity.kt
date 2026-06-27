package com.example.pawmesh

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pawmesh.databinding.UplodeLayoutBinding

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: UplodeLayoutBinding
    private var currentUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            currentUri = it
            showPhoto(it)
        }
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

        intent.getStringExtra("photo_uri")?.let {
            currentUri = Uri.parse(it)
            showPhoto(currentUri!!)
        }

        binding.btnSelect2.setOnClickListener { openGallery() }

        binding.btnSelect1.setOnClickListener {
            val uri = currentUri ?: run {
                Log.w("PawMesh_Photo", "2. 사진 미선택 상태로 버튼 클릭")
                return@setOnClickListener
            }
            val dogName = binding.sampleEditText.text.toString().trim()
                .takeIf { it.isNotEmpty() && it != "이름을 입력하세요" } ?: ""
            Log.d("PawMesh_Photo", "2. UploadActivity → SplashActivity | uri=$uri, dogName=$dogName")

            val intent = Intent(this, SplashActivity::class.java).apply {
                putExtra("photo_uri", uri.toString())
                putExtra("dog_name", dogName)
            }
            startActivity(intent)
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