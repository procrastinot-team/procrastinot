package com.github.mateo762.myapplication.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mateo762.myapplication.databinding.ItemSearchBinding
import com.github.mateo762.myapplication.search.SearchItemViewHolder

/**
 * Recycler view adapter for the profile gallery.
 */
class SearchViewAdapter(
    private val items: List<SearchItem>, private val onUserItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchItemViewHolder>() {

    private val filteredItems = mutableListOf<SearchItem>()


    init {
        filteredItems.addAll(items)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchItemViewHolder {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        filteredItems.let { items ->
            holder.bind(items[position], onUserItemClick)
        }
    }

    fun filter(query: String) {
        filteredItems.clear()
        items.forEach { item ->
            if (item.name.contains(query, true) || item.description.contains(query, true)) {
                filteredItems.add(item)
            }
        }
        notifyDataSetChanged()
    }
}