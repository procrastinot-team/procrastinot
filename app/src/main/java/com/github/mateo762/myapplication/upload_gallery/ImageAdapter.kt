package com.github.mateo762.myapplication.upload_gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mateo762.myapplication.R
import com.github.mateo762.myapplication.models.HabitImageEntity

class ImageAdapter(private val imagesList: ArrayList<HabitImageEntity>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.habitImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_gallery, parent, false)

        return ImageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        //Using Glide to automatically fetch the images stored in the urls and display them
        Glide.with(context).load(imagesList[position].url).into(holder.image)
    }


}