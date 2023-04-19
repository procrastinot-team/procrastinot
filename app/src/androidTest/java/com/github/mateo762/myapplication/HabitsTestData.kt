package com.github.mateo762.myapplication

import com.github.mateo762.myapplication.models.Habit
import com.github.mateo762.myapplication.models.HabitImage
import java.time.DayOfWeek
import java.time.LocalDateTime

object TestData {
    val hardCodedHabits: List<Habit> = listOf(
        Habit(
            id = "1",
            name = "Read",
            days = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            startTime = "08:00",
            endTime = "08:30"
        ),
        Habit(
            id = "2",
            name = "Drink water",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "09:00",
            endTime = "17:00"
        ),
        Habit(
            id = "3",
            name = "Exercise",
            days = arrayListOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
            startTime = "18:00",
            endTime = "19:00"
        ),
        Habit(
            id = "4",
            name = "Meditate",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "07:15"
        ),
        Habit(
            id = "5",
            name = "Walk dog",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "20:00",
            endTime = "20:10"
        ),
        Habit(
            id = "6",
            name = "Ride bike",
            days = arrayListOf(
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "09:10"
        )
    )

    val noTodayHabits: List<Habit> = listOf(
        Habit(
            id = "1",
            name = "Read",
            days = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            startTime = "08:00",
            endTime = "08:30"
        ),
        Habit(
            id = "2",
            name = "Drink water",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "09:00",
            endTime = "17:00"
        ),
        Habit(
            id = "3",
            name = "Exercise",
            days = arrayListOf(DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
            startTime = "18:00",
            endTime = "19:00"
        ),
        Habit(
            id = "4",
            name = "Meditate",
            days = arrayListOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "07:15"
        ),
        Habit(
            id = "5",
            name = "Walk dog",
            days = arrayListOf(
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "20:00",
            endTime = "20:10"
        ),
        Habit(
            id = "6",
            name = "Ride bike",
            days = arrayListOf(
                DayOfWeek.THURSDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ),
            startTime = "07:00",
            endTime = "09:10"
        )
    )

    val emptyHabits: List<Habit> = listOf()

    val hardCodedImages: List<HabitImage> = listOf(
        HabitImage(
            habitId = "3",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 4, 15, 17, 0).toString()
        ),
        HabitImage(
            habitId = "3",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 4, 14, 17, 0).toString()
        ),
        HabitImage(
            habitId = "3",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 2, 16, 17, 0).toString()
        ),
        HabitImage(
            habitId = "3",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 1, 16, 17, 0).toString()
        ),
        HabitImage(
            habitId = "4",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 4, 15, 17, 0).toString()
        ),
        HabitImage(
            habitId = "4",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 4, 14, 17, 0).toString()
        ),
        HabitImage(
            habitId = "4",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 2, 16, 17, 0).toString()
        ),
        HabitImage(
            habitId = "4",
            url = "https://5.imimg.com/data5/WA/NV/LI/SELLER-52971039/apple-indian-500x500.jpg",
            date = LocalDateTime.of(2022, 1, 16, 17, 0).toString()
        )
    )

    val testHabits: List<Habit> = listOf(
        Habit(
            id = "1",
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
        Habit(
            id = "2",
            name = "Read",
            days = arrayListOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            startTime = "08:00",
            endTime = "09:00"
        ),
        Habit(
            id = "3",
            name = "Exercise",
            days = arrayListOf(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
            startTime = "18:00",
            endTime = "19:00"
        ),
        Habit(
            id = "4",
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
        Habit(
            id = "5",
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
        Habit(
            id = "6",
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