package com.example.pawmesh

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmesh.adapter.Friend
import com.example.pawmesh.adapter.FriendAdapter

class FriendListActivity : AppCompatActivity() {

    private lateinit var adapter: FriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        adapter = FriendAdapter(emptyList()) { friend ->
            Toast.makeText(this, "${friend.name}에게 산책 초대를 보냈어요", Toast.LENGTH_SHORT).show()
            // TODO: 산책 초대 API 호출
        }

        findViewById<RecyclerView>(R.id.rvFriends).apply {
            layoutManager = LinearLayoutManager(this@FriendListActivity)
            adapter = this@FriendListActivity.adapter
        }

        // TODO: 팀원 API 연동 후 아래 주석 해제
        // lifecycleScope.launch {
        //     val friends = RetrofitClient.api.getFriends()
        //     updateFriendList(friends)
        // }
        updateFriendList(emptyList())
    }

    fun updateFriendList(friends: List<Friend>) {
        val isEmpty = friends.isEmpty()

        listOf(R.id.imageView3, R.id.imageView4, R.id.tvEmpty1, R.id.tvEmpty2)
            .forEach { id -> findViewById<View>(id).visibility = if (isEmpty) View.VISIBLE else View.GONE }

        findViewById<RecyclerView>(R.id.rvFriends).visibility = if (isEmpty) View.GONE else View.VISIBLE

        adapter = FriendAdapter(friends) { friend ->
            Toast.makeText(this, "${friend.name}에게 산책 초대를 보냈어요", Toast.LENGTH_SHORT).show()
            // TODO: 산책 초대 API 호출
        }
        findViewById<RecyclerView>(R.id.rvFriends).adapter = adapter
    }
}
