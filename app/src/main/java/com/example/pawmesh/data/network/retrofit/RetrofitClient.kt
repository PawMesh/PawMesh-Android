package com.example.pawmesh.data.network.retrofit

import com.example.pawmesh.data.network.api.ApiService
import com.example.pawmesh.data.network.api.AuthApi
import com.example.pawmesh.data.network.api.MapApi
import com.example.pawmesh.data.network.api.UserApi
import com.example.pawmesh.data.network.api.WalkRequestApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://3.107.200.236:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val instance: ApiService by lazy { retrofit.create(ApiService::class.java) }

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val mapApi: MapApi = retrofit.create(MapApi::class.java)
    val userApi: UserApi = retrofit.create(UserApi::class.java)
    val walkRequestApi: WalkRequestApi = retrofit.create(WalkRequestApi::class.java)
}