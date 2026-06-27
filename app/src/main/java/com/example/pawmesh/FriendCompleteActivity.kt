package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FriendCompleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_complete)

        val dogName = intent.getStringExtra(EXTRA_DOG_NAME) ?: "강아지"
        val walkCount = intent.getIntExtra(EXTRA_WALK_COUNT, 1)
        val level = intent.getIntExtra(EXTRA_LEVEL, 1)
        val progress = intent.getIntExtra(EXTRA_PROGRESS, 30)
        val levelStartLabel = intent.getStringExtra(EXTRA_LEVEL_START_LABEL) ?: "첫인사"
        val levelEndLabel = intent.getStringExtra(EXTRA_LEVEL_END_LABEL) ?: "산책 친구"

        findViewById<TextView>(R.id.tvSubtitle).text = "${dogName}와 이제 산책 친구예요!"
        findViewById<TextView>(R.id.tvWalkCount).text = walkCount.toString()
        findViewById<TextView>(R.id.tvLevel).text = "레벨 $level"
        findViewById<ProgressBar>(R.id.progressBar).progress = progress
        findViewById<TextView>(R.id.tvLevelStart).text = levelStartLabel
        findViewById<TextView>(R.id.tvLevelEnd).text = levelEndLabel

        findViewById<Button>(R.id.btnFriendList).setOnClickListener {
            startActivity(Intent(this, FriendListActivity::class.java))
        }

        findViewById<TextView>(R.id.tvHome).setOnClickListener { navigateHome() }
    }

    private fun navigateHome() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        })
        finish()
    }

    companion object {
        const val EXTRA_DOG_NAME = "extra_dog_name"
        const val EXTRA_WALK_COUNT = "extra_walk_count"
        const val EXTRA_LEVEL = "extra_level"
        const val EXTRA_PROGRESS = "extra_progress"
        const val EXTRA_LEVEL_START_LABEL = "extra_level_start_label"
        const val EXTRA_LEVEL_END_LABEL = "extra_level_end_label"
    }
}
