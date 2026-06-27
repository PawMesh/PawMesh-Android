package com.example.pawmesh

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.pawmesh.data.network.retrofit.RetrofitClient
import com.example.pawmesh.databinding.UplodeLayoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: UplodeLayoutBinding
    private var selectedUri: Uri? = null

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

        intent.getStringExtra("photo_uri")?.let {
            showPhoto(Uri.parse(it))
        }

        binding.btnSelect2.setOnClickListener {
            openGallery()
        }

        binding.btnSelect1.setOnClickListener {
            val petName = binding.sampleEditText.text.toString().trim()
            if (petName.isEmpty()) {
                Toast.makeText(this, "강아지 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val uri = selectedUri ?: run {
                Toast.makeText(this, "사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadPhotoAndNavigate(uri, petName)
        }
    }

    private fun uploadPhotoAndNavigate(uri: Uri, petName: String) {
        lifecycleScope.launch {
            try {
                val imagePart = withContext(Dispatchers.IO) {
                    val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                    val inputStream = contentResolver.openInputStream(uri)
                        ?: throw IllegalStateException("사진을 불러올 수 없어요.")
                    val bytes = inputStream.use { it.readBytes() }
                    val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
                    val extension = mimeType.substringAfter("/")
                    MultipartBody.Part.createFormData("image", "pet_photo.$extension", requestBody)
                }

                val response = RetrofitClient.authApi.uploadPetImage(imagePart)

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val signupToken = response.body()!!.data.signupToken
                    val intent = Intent(this@UploadActivity, SplashActivity::class.java).apply {
                        putExtra("signup_token", signupToken)
                        putExtra("pet_name", petName)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("UploadActivity", "upload failed: HTTP ${response.code()}, isSuccess=${response.body()?.isSuccess}, error=$errorBody")
                    Toast.makeText(this@UploadActivity, "실패: HTTP ${response.code()} / ${response.body()?.message ?: errorBody}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("UploadActivity", "upload error: ${e::class.simpleName} - ${e.message}", e)
                Toast.makeText(this@UploadActivity, "오류: ${e::class.simpleName} - ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showPhoto(uri: Uri) {
        selectedUri = uri
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
