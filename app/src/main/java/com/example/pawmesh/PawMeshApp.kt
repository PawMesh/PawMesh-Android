package com.example.pawmesh

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class PawMeshApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, "247cb7d9cc222265ce4b4d11a6b410f1")
    }
}
