package com.example.pawmesh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmesh.R

data class Friend(
    val name: String,
    val walkCount: Int,
    val level: Int
)

class FriendAdapter(
    private val friends: List<Friend>,
    private val onInviteClick: (Friend) -> Unit
) : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDogName: TextView = view.findViewById(R.id.tvDogName)
        val tvDogInfo: TextView = view.findViewById(R.id.tvDogInfo)
        val btnInvite: TextView = view.findViewById(R.id.btnInvite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]
        holder.tvDogName.text = friend.name
        holder.tvDogInfo.text = "레벨 ${friend.level} · ${friend.walkCount}번 함께 산책함"
        holder.btnInvite.setOnClickListener { onInviteClick(friend) }
    }

    override fun getItemCount() = friends.size
}
