package com.github.mateo762.myapplication.search

import androidx.recyclerview.widget.RecyclerView
import com.github.mateo762.myapplication.databinding.ItemSearchBinding
import com.github.mateo762.myapplication.profile.SearchItem

class SearchItemViewHolder(private var binding: ItemSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Method called on onBindViewHolder in the adapter, that rerenders the view holder state.
     */
    fun bind(searchItem: SearchItem, onUserItemClick: (String) -> Unit) {
        binding.searchName.text = searchItem.name
        binding.searchDescription.text = searchItem.description

        binding.root.setOnClickListener {
            onUserItemClick(searchItem.userId)
        }
    }
}