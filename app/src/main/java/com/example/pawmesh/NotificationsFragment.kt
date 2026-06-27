package com.example.pawmesh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawmesh.adapter.NotificationAdapter
import com.example.pawmesh.adapter.WalkNotification

class NotificationsFragment : Fragment() {

    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.activity_notification, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NotificationAdapter(emptyList())
        view.findViewById<RecyclerView>(R.id.rvNotifications).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NotificationsFragment.adapter
        }

        // TODO: 팀원 API 연동 후 아래 주석 해제
        // viewLifecycleOwner.lifecycleScope.launch {
        //     val notifications = RetrofitClient.api.getNotifications()
        //     updateNotificationList(notifications)
        // }
        updateNotificationList(emptyList())
    }

    fun updateNotificationList(notifications: List<WalkNotification>) {
        val view = requireView()
        val isEmpty = notifications.isEmpty()

        view.findViewById<RecyclerView>(R.id.rvNotifications).visibility = if (isEmpty) View.GONE else View.VISIBLE
        view.findViewById<View>(R.id.tvEmpty).visibility = if (isEmpty) View.VISIBLE else View.GONE

        adapter = NotificationAdapter(notifications)
        view.findViewById<RecyclerView>(R.id.rvNotifications).adapter = adapter
    }

    companion object {
        fun newInstance() = NotificationsFragment()
    }
}
