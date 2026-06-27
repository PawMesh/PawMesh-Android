package com.example.pawmesh

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class WalkRequestFragment2 : Fragment() {

    companion object {
        private const val ARG_SESSION_ID = "session_id"

        fun newInstance(sessionId: Int) = WalkRequestFragment2().apply {
            arguments = Bundle().apply {
                putInt(ARG_SESSION_ID, sessionId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_walk_request2, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(
                Intent(requireContext(), WalkCompleteActivity::class.java).apply {
                    putExtra(WalkCompleteActivity.EXTRA_MY_DOG_NAME, "내 강아지")
                    putExtra(WalkCompleteActivity.EXTRA_DOG_NAME, "친구 강아지")
                    putExtra(WalkCompleteActivity.EXTRA_WALK_COUNT, 1)
                }
            )
            parentFragmentManager.popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        view.findViewById<TextView>(R.id.textView3).setOnClickListener {
            parentFragmentManager.popBackStack(null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}