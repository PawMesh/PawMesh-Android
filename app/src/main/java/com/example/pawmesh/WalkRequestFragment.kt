package com.example.pawmesh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.pawmesh.data.network.dto.response.NearbySessionDto

class WalkRequestFragment : Fragment() {

    companion object {
        private const val ARG_DOG_ID = "dog_id"
        private const val ARG_IMAGE_URL = "image_url"
        private const val ARG_SESSION_ID = "session_id"

        fun newInstance(session: NearbySessionDto) = WalkRequestFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_DOG_ID, session.dogId ?: 0)
                putString(ARG_IMAGE_URL, session.characterImageUrl)
                putInt(ARG_SESSION_ID, session.walkSessionId ?: 0)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_walk_request, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrl = arguments?.getString(ARG_IMAGE_URL)
        val sessionId = arguments?.getInt(ARG_SESSION_ID) ?: 0

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this).load(imageUrl)
                .into(view.findViewById(R.id.iv_dog2))
        }

        view.findViewById<Button>(R.id.button).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WalkRequestFragment2.newInstance(sessionId))
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<TextView>(R.id.textView3).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}