package com.example.pawmesh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmesh.adapter.Friend
import com.example.pawmesh.adapter.FriendAdapter

class FriendsFragment : Fragment() {

    private lateinit var adapter: FriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.activity_friend_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FriendAdapter(emptyList()) { friend ->
            Toast.makeText(requireContext(), "${friend.name}에게 산책 초대를 보냈어요", Toast.LENGTH_SHORT).show()
            // TODO: 산책 초대 API 호출
        }

        view.findViewById<RecyclerView>(R.id.rvFriends).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FriendsFragment.adapter
        }

        // TODO: 팀원 API 연동 후 아래 주석 해제
        // viewLifecycleOwner.lifecycleScope.launch {
        //     val friends = RetrofitClient.api.getFriends()
        //     updateFriendList(friends)
        // }
        updateFriendList(emptyList())
    }

    fun updateFriendList(friends: List<Friend>) {
        val view = requireView()
        val isEmpty = friends.isEmpty()

        listOf(R.id.imageView3, R.id.imageView4, R.id.tvEmpty1, R.id.tvEmpty2)
            .forEach { id -> view.findViewById<View>(id).visibility = if (isEmpty) View.VISIBLE else View.GONE }

        view.findViewById<RecyclerView>(R.id.rvFriends).visibility = if (isEmpty) View.GONE else View.VISIBLE

        adapter = FriendAdapter(friends) { friend ->
            Toast.makeText(requireContext(), "${friend.name}에게 산책 초대를 보냈어요", Toast.LENGTH_SHORT).show()
            // TODO: 산책 초대 API 호출
        }
        view.findViewById<RecyclerView>(R.id.rvFriends).adapter = adapter
    }

    companion object {
        fun newInstance() = FriendsFragment()
    }
}
