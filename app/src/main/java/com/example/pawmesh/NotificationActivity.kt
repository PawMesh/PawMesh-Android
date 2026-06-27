package com.example.pawmesh

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmesh.adapter.NotificationAdapter
import com.example.pawmesh.adapter.WalkNotification

class NotificationActivity : AppCompatActivity() {

    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        adapter = NotificationAdapter(emptyList())
        findViewById<RecyclerView>(R.id.rvNotifications).apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            adapter = this@NotificationActivity.adapter
        }

        // TODO: 팀원 API 연동 후 아래 주석 해제
        // lifecycleScope.launch {
        //     val notifications = RetrofitClient.api.getNotifications()
        //     updateNotificationList(notifications)
        // }
        updateNotificationList(emptyList())
    }

    fun updateNotificationList(notifications: List<WalkNotification>) {
        val isEmpty = notifications.isEmpty()
        val rv = findViewById<RecyclerView>(R.id.rvNotifications)
        val tvEmpty = findViewById<View>(R.id.tvEmpty)

        rv.visibility = if (isEmpty) View.GONE else View.VISIBLE
        tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE

        adapter = NotificationAdapter(notifications)
        rv.adapter = adapter
    }
}
