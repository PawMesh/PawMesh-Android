package com.example.pawmesh

import android.app.Application
import com.example.pawmesh.data.local.TokenManager

class PawMeshApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TokenManager.init(this)

        // 테스트용 임시 토큰 (나중에 로그인 구현하면 삭제)
        TokenManager.saveTokens(
            accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzgyNTgzOTk5LCJleHAiOjE3ODI1ODc1OTl9.XKVODJVn_p5CfkfiHEFezo2WBI-1Q164IqxQ3Gw3VkDv2JIkSqXU1UyVnqdWWR9ioy_T6UZfGTQxb2hGFcNLbA",
            refreshToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzgyNTgzOTk5LCJleHAiOjE3ODM3OTM1OTl9.XzoAIjPo_3zo8WFGHQ4NUOA7FqaMck7qn6R8HqVLuePDT3Ll8Fx5yTx1olOgeZAkQDAqlhVq6kjIh7eWuJY0sQ"
        )
    }
}