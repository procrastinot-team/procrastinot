package com.github.mateo762.myapplication.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mateo762.myapplication.databinding.ItemProfileHabitBinding
import com.github.mateo762.myapplication.models.HabitEntity

class ProfileHabitsAdapter : RecyclerView.Adapter<ProfileHabitsViewHolder>() {

    var habits: ArrayList<HabitEntity>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHabitsViewHolder {
        val binding =
            ItemProfileHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileHabitsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return habits?.size ?: 0
    }

    override fun onBindViewHolder(holder: ProfileHabitsViewHolder, position: Int) {
        habits?.let {
            holder.bind(it[position])
        }
    }

}