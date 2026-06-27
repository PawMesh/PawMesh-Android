package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class WalkCompleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_complete)

        val myDogName = intent.getStringExtra(EXTRA_MY_DOG_NAME) ?: "내 강아지"
        val dogName = intent.getStringExtra(EXTRA_DOG_NAME) ?: "강아지"
        val walkCount = intent.getIntExtra(EXTRA_WALK_COUNT, 1)

        findViewById<TextView>(R.id.tvSubtitle).text = "${dogName}와 ${walkCount}번째 산책을 완료했어요"
        findViewById<TextView>(R.id.tvRecordTitle).text = "${myDogName} & ${dogName}의 산책 기록"
        intent.getStringExtra(EXTRA_TIME)?.let { findViewById<TextView>(R.id.tvTime).text = it }
        intent.getStringExtra(EXTRA_DISTANCE)?.let { findViewById<TextView>(R.id.tvDistance).text = it }
        intent.getStringExtra(EXTRA_CALORIE)?.let { findViewById<TextView>(R.id.tvCalorie).text = it }
        intent.getStringExtra(EXTRA_DATE)?.let { findViewById<TextView>(R.id.tvDate).text = it }

        findViewById<MaterialButton>(R.id.btnShare).setOnClickListener {
            val distance = intent.getStringExtra(EXTRA_DISTANCE) ?: ""
            val shareText = "${dogName}와 ${walkCount}번째 산책 완료! $distance 걸었어요 🐾"
            startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    },
                    "산책 기록 공유하기"
                )
            )
        }

        findViewById<Button>(R.id.btnFriend).setOnClickListener {
            startActivity(Intent(this, FriendCompleteActivity::class.java).apply {
                putExtra(FriendCompleteActivity.EXTRA_DOG_NAME, dogName)
                putExtra(FriendCompleteActivity.EXTRA_WALK_COUNT, walkCount)
            })
        }

        findViewById<TextView>(R.id.tvSkip).setOnClickListener { navigateHome() }
    }

    private fun navigateHome() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        })
        finish()
    }

    companion object {
        const val EXTRA_MY_DOG_NAME = "extra_my_dog_name"
        const val EXTRA_DOG_NAME = "extra_dog_name"
        const val EXTRA_WALK_COUNT = "extra_walk_count"
        const val EXTRA_TIME = "extra_time"
        const val EXTRA_DISTANCE = "extra_distance"
        const val EXTRA_CALORIE = "extra_calorie"
        const val EXTRA_DATE = "extra_date"
    }
}
