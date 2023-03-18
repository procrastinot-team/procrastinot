package com.github.procrastinotteam.procrastinot.profile

import androidx.recyclerview.widget.RecyclerView
import com.github.procrastinotteam.procrastinot.databinding.ItemProfileGalleryBinding

/**
 * View holder class for the profile gallery item.
 */
class ProfileGalleryItemViewHolder(private var binding: ItemProfileGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Method called on onBindViewHolder in the adapter, that rerenders the view holder state.
     */
    fun bind(profileGalleryItem: ProfileGalleryItem) {
        binding.habitImage.setImageResource(profileGalleryItem.image)
    }
}