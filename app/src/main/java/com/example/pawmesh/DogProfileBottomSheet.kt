package com.example.pawmesh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pawmesh.data.network.dto.response.NearbySessionDto
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class DogProfileBottomSheet : BottomSheetDialogFragment() {

    private var session: NearbySessionDto? = null

    companion object {
        fun newInstance(session: NearbySessionDto): DogProfileBottomSheet {
            return DogProfileBottomSheet().apply {
                this.session = session
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.bottom_sheet_dog_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val s = session ?: return

        val ivDog = view.findViewById<android.widget.ImageView>(R.id.ivDogCharacter)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupTags)
        val btnRequest = view.findViewById<android.widget.Button>(R.id.btnWalkRequest)

        if (!s.characterImageUrl.isNullOrEmpty()) {
            Glide.with(this).load(s.characterImageUrl).into(ivDog)
        }

        s.tags?.forEach { tag ->
            val chip = Chip(requireContext()).apply {
                text = tag
                isClickable = false
                setChipBackgroundColorResource(android.R.color.white)
            }
            chipGroup.addView(chip)
        }

        btnRequest.setOnClickListener {
            dismiss()
        }
    }
}