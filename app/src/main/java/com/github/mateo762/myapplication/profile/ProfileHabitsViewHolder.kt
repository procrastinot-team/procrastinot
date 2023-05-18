package com.github.mateo762.myapplication.profile

import androidx.recyclerview.widget.RecyclerView
import com.github.mateo762.myapplication.databinding.ItemProfileHabitBinding
import com.github.mateo762.myapplication.models.HabitEntity

class ProfileHabitsViewHolder(private var binding: ItemProfileHabitBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(habit: HabitEntity) {
        binding.habit.text = habit.name.plus(": ").plus(habit.days.joinToString(", ") { it.name })
    }
}