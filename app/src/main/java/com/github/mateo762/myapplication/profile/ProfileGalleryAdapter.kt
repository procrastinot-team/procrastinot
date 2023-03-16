package com.github.mateo762.myapplication.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mateo762.myapplication.databinding.ItemProfileGalleryBinding

/**
 * Recycler view adapter for the profile gallery.
 */
class ProfileGalleryAdapter : RecyclerView.Adapter<ProfileGalleryItemViewHolder>() {

    var galleryItems: ArrayList<ProfileGalleryItem>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileGalleryItemViewHolder {
        val binding =
            ItemProfileGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileGalleryItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return galleryItems?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProfileGalleryItemViewHolder, position: Int) {
        galleryItems?.let { items ->
            holder.bind(items[position])
        }
    }
}