package com.github.mateo762.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import com.github.mateo762.myapplication.models.HabitEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.DayOfWeek
import java.util.*


// TODO: This class should be deleted ASAP
// TODO: This class should be deleted ASAP

private val user = FirebaseAuth.getInstance().currentUser

@RequiresApi(Build.VERSION_CODES.O)
//TODO: Integrate the getHabits properly
fun getHabits(callback: (List<HabitEntity>) -> Unit) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val habitListRef = FirebaseDatabase.getInstance().getReference("users/$currentUserId/habit_list")

    habitListRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val habitList = mutableListOf<HabitEntity>()
            for (habitSnapshot in snapshot.children) {
                val id = habitSnapshot.child("id").value as String
                val name = habitSnapshot.child("name").value as String
                val days = habitSnapshot.child("days").children.mapNotNull { dayOfWeek ->
                    when (dayOfWeek.getValue(String::class.java)) {
                        "MONDAY" -> DayOfWeek.MONDAY
                        "TUESDAY" -> DayOfWeek.TUESDAY
                        "WEDNESDAY" -> DayOfWeek.WEDNESDAY
                        "THURSDAY" -> DayOfWeek.THURSDAY
                        "FRIDAY" -> DayOfWeek.FRIDAY
                        "SATURDAY" -> DayOfWeek.SATURDAY
                        "SUNDAY" -> DayOfWeek.SUNDAY
                        else -> null
                    }
                }
                val startTime = habitSnapshot.child("startTime").value as String
                val endTime = habitSnapshot.child("endTime").value as String

                val habit = HabitEntity(id, name, days, startTime, endTime)
                habitList.add(habit)
            }
            callback(habitList)
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error here
            callback(emptyList())
        }
    })
}

@RequiresApi(Build.VERSION_CODES.O)
fun getHardCodedHabits(): List<HabitEntity> {

    return listOf(
        HabitEntity(
            id = "7254b3c2-1d9f-4269-82a9-d761cc979a6a",
            name = "Stretch",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "07:15"
        ),
        HabitEntity(
            id = "7254b3c2-1d9f-4269-82a9-d761cc979a6b",
            name = "Read",
            days = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            startTime = "08:00",
            endTime = "09:00"
        ),
        HabitEntity(
            id = "7254b3c2-1d9f-4269-82a9-d761cc979a6c",
            name = "Exercise",
            days = arrayListOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
            startTime = "18:00",
            endTime = "19:00"
        ),
        HabitEntity(
            id = "7254b3c2-1d9f-4269-82a9-d761cc979a6d",
            name = "Walk",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "06:00",
            endTime = "07:00"
        ),
        HabitEntity(
            id = "7254b3c2-1d9f-4269-82a9-d761cc979a6e",
            name = "Study",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "20:00",
            endTime = "22:00"
        ),
        HabitEntity(
            id = "7254b3c2-1d9f-4269-82a9-d761cc979a6f",
            name = "Ride bike",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
            ),
            startTime = "14:25",
            endTime = "16:00"
        )
    )
}

fun getHardCodedImages(): List<String> {
    return listOf(
        "https://firebasestorage.googleapis.com/v0/b/procrastinot-backend.appspot.com/o/users%2FmeCtC5TWlEcBDcy5Ss6hzf8Qg723%2Fimages%2Fdb84c733-1c53-4c71-b6b5-e07a1ef6a058.jpg?alt=media&token=ad10cd50-8858-4d73-b8bf-91c82b8f5149",
        "https://firebasestorage.googleapis.com/v0/b/procrastinot-backend.appspot.com/o/users%2FmeCtC5TWlEcBDcy5Ss6hzf8Qg723%2Fimages%2Fdb84c733-1c53-4c71-b6b5-e07a1ef6a058.jpg?alt=media&token=ad10cd50-8858-4d73-b8bf-91c82b8f5149",
        "https://firebasestorage.googleapis.com/v0/b/procrastinot-backend.appspot.com/o/users%2FmeCtC5TWlEcBDcy5Ss6hzf8Qg723%2Fimages%2Fdb84c733-1c53-4c71-b6b5-e07a1ef6a058.jpg?alt=media&token=ad10cd50-8858-4d73-b8bf-91c82b8f5149"
    )
}



@RequiresApi(Build.VERSION_CODES.O)
fun getOneHabit(): List<HabitEntity>{
    return listOf(
        HabitEntity(
        id = UUID.randomUUID().toString(),
        name = "Stretch",
        days = arrayListOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ),
        startTime = "14:00",
        endTime = "15:30"
    )
    )
}