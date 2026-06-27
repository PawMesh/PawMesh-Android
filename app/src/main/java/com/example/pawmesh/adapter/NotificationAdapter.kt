package com.example.pawmesh.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmesh.R

enum class NotificationType { FRIEND, WALK_COMPLETE }

data class WalkNotification(
    val type: NotificationType,
    val message: String,
    val time: String
)

class NotificationAdapter(
    private val notifications: List<WalkNotification>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgIcon: ImageView = view.findViewById(R.id.imgNotifIcon)
        val tvMessage: TextView = view.findViewById(R.id.tvNotifMessage)
        val tvTime: TextView = view.findViewById(R.id.tvNotifTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notif = notifications[position]
        holder.tvMessage.text = notif.message
        holder.tvTime.text = notif.time
        holder.imgIcon.setImageResource(
            when (notif.type) {
                NotificationType.FRIEND -> R.drawable.noti_heart
                NotificationType.WALK_COMPLETE -> R.drawable.noti_check
            }
        )
    }

    override fun getItemCount() = notifications.size
}
