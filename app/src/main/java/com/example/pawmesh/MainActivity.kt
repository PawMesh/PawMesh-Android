package com.example.pawmesh

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pawmesh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigationView.itemActiveIndicatorColor =
            ColorStateList.valueOf(Color.parseColor("#DBEAFE"))

        if (savedInstanceState == null) {
            // TODO: 에뮬레이터 테스트용 - 실제 배포 시 MapFragment.newInstance()로 교체
            showFragment(FriendsFragment.newInstance())
            // showFragment(MapFragment.newInstance()) // 실제 코드
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.navigation_map -> MapFragment.newInstance()
                R.id.navigation_friends -> FriendsFragment.newInstance()
                R.id.navigation_notifications -> NotificationsFragment.newInstance()
                R.id.navigation_profile -> ProfileFragment.newInstance()
                else -> return@setOnItemSelectedListener false
            }
            showFragment(fragment)
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
