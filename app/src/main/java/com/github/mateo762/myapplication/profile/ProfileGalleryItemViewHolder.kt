package com.github.mateo762.myapplication.profile

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mateo762.myapplication.databinding.ItemProfileGalleryBinding
import com.github.mateo762.myapplication.models.HabitImageEntity

/**
 * View holder class for the profile gallery item.
 */
class ProfileGalleryItemViewHolder(private var binding: ItemProfileGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Method called on onBindViewHolder in the adapter, that rerenders the view holder state.
     */
    fun bind(habitImage: HabitImageEntity) {
        Glide.with(binding.root.context).load(habitImage.url).into(binding.habitImage)
    }
}