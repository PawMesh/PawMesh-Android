package com.example.pawmesh

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 팀원 API 연동 후 실제 데이터로 교체
        bindDogProfile(
            name = "덕배",
            type = "잉글랜드 쉽독",
            personalities = listOf("온순함", "호기심 많음")
        )
        bindStats(walkCount = 0, friendCount = 0)

        view.findViewById<View>(R.id.btnEditProfile).setOnClickListener {
            startActivity(Intent(requireContext(), EditDogProfileActivity::class.java))
        }

        view.findViewById<TextView>(R.id.tvOwnerProfile).setOnClickListener {
            // TODO: 보호자 프로필 화면으로 이동
        }

        view.findViewById<TextView>(R.id.tvDeleteAccount).setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    fun bindDogProfile(name: String, type: String, personalities: List<String>) {
        val view = requireView()
        view.findViewById<TextView>(R.id.tvDogName).text = name
        view.findViewById<TextView>(R.id.tvDogType).text = type

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupPersonality)
        chipGroup.removeAllViews()
        val font = ResourcesCompat.getFont(requireContext(), R.font.mona10x12)
        personalities.forEach { personality ->
            val chip = Chip(requireContext()).apply {
                text = personality
                typeface = font
                isClickable = false
                isCheckable = false
                chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#EFF6FF"))
                setTextColor(Color.parseColor("#3D6FF0"))
            }
            chipGroup.addView(chip)
        }
    }

    fun bindStats(walkCount: Int, friendCount: Int) {
        val view = requireView()
        view.findViewById<TextView>(R.id.tvWalkCount).text = walkCount.toString()
        view.findViewById<TextView>(R.id.tvFriendCount).text = friendCount.toString()
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("계정 삭제")
            .setMessage("정말로 계정을 삭제하시겠어요?\n이 작업은 되돌릴 수 없어요.")
            .setPositiveButton("삭제하기") { _, _ ->
                // TODO: 계정 삭제 API 호출
            }
            .setNegativeButton("취소", null)
            .show()
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
