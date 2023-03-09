package com.github.mateo762.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.github.mateo762.myapplication.Habit
import com.github.mateo762.myapplication.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HabitFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.habit_submit, container, false)
        val nameOfHabit = view.findViewById<EditText>(R.id.input1)
        val timeOfHabitStart = view.findViewById<EditText>(R.id.input2)
        val timeOfHabitEnd = view.findViewById<EditText>(R.id.input3)
        val mon = view.findViewById<CheckBox>(R.id.checkBox1)
        val tue = view.findViewById<CheckBox>(R.id.checkBox2)
        val wed = view.findViewById<CheckBox>(R.id.checkBox3)
        val thu = view.findViewById<CheckBox>(R.id.checkBox4)
        val fri = view.findViewById<CheckBox>(R.id.checkBox5)
        val sat = view.findViewById<CheckBox>(R.id.checkBox6)
        val sun = view.findViewById<CheckBox>(R.id.checkBox7)
        val submit = view.findViewById<Button>(R.id.button1)



        submit.setOnClickListener{

            var habit = Habit(
                nameOfHabit.text.toString(),
                timeOfHabitStart.text.toString(),
                timeOfHabitEnd.text.toString(),
                mon.isChecked,
                tue.isChecked,
                wed.isChecked,
                thu.isChecked,
                fri.isChecked,
                sat.isChecked,
                sun.isChecked
            )
            println("Clicked Submit $habit")
            val db: DatabaseReference = Firebase.database.reference
            db.child(nameOfHabit.text.toString()).setValue(habit).addOnSuccessListener {
                println("Success")
            }.addOnFailureListener {
                println(it.message)
            }




        }



        return view
    }
}