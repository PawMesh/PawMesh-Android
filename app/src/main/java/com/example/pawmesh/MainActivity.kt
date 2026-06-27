package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.pawmesh.adapter.NotificationType
import com.example.pawmesh.adapter.WalkNotification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val today = SimpleDateFormat("M월 d일 EEEE", Locale.KOREAN).format(Date())

        findViewById<Button>(R.id.btnTestWalkComplete).setOnClickListener {
            startActivity(Intent(this, WalkCompleteActivity::class.java).apply {
                putExtra(WalkCompleteActivity.EXTRA_MY_DOG_NAME, "두부")
                putExtra(WalkCompleteActivity.EXTRA_DOG_NAME, "보리")
                putExtra(WalkCompleteActivity.EXTRA_WALK_COUNT, 1)
                putExtra(WalkCompleteActivity.EXTRA_TIME, "45분")
                putExtra(WalkCompleteActivity.EXTRA_DISTANCE, "2.3km")
                putExtra(WalkCompleteActivity.EXTRA_CALORIE, "185kcal")
                putExtra(WalkCompleteActivity.EXTRA_DATE, today)
            })
        }

        findViewById<Button>(R.id.btnTestFriendComplete).setOnClickListener {
            startActivity(Intent(this, FriendCompleteActivity::class.java).apply {
                putExtra(FriendCompleteActivity.EXTRA_DOG_NAME, "보리")
                putExtra(FriendCompleteActivity.EXTRA_WALK_COUNT, 1)
                putExtra(FriendCompleteActivity.EXTRA_LEVEL, 2)
                putExtra(FriendCompleteActivity.EXTRA_PROGRESS, 30)
                putExtra(FriendCompleteActivity.EXTRA_LEVEL_START_LABEL, "첫인사")
                putExtra(FriendCompleteActivity.EXTRA_LEVEL_END_LABEL, "산책 친구")
            })
        }

        findViewById<Button>(R.id.btnTestFriendList).setOnClickListener {
            startActivity(Intent(this, FriendListActivity::class.java))
        }

        findViewById<Button>(R.id.btnTestNotification).setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }
}
