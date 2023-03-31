package com.github.mateo762.myapplication.search

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.databinding.ItemSearchBinding
import com.github.mateo762.myapplication.profile.ProfileGalleryItem
import com.github.mateo762.myapplication.profile.SearchItem

class SearchItemViewHolder(private var binding: ItemSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Method called on onBindViewHolder in the adapter, that rerenders the view holder state.
     */
    fun bind(searchItem: SearchItem) {
        binding.searchName.setText(searchItem.name)
        binding.searchDescription.setText(searchItem.description)
    }
}