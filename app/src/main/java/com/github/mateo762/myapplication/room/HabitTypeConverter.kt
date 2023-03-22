package com.github.mateo762.myapplication.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.DayOfWeek

class HabitTypeConverter {
    private var gson = Gson()

    @TypeConverter
    fun stringToHabitEntity(data: String): List<HabitEntity> {
/*        if (data == null) {
            return Collections.emptyList()
        }*/
        val listType: Type = object : TypeToken<List<HabitEntity?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun habitEntityListToString(habitsList: List<HabitEntity?>?): String {
        return gson.toJson(habitsList)
    }

    @TypeConverter
    fun habitDaysToString(daysList: List<DayOfWeek?>?): String {
        return gson.toJson(daysList)
    }

    @TypeConverter
    fun stringToHabitDays(data: String): List<DayOfWeek>? {
        /*if (data == null) {
            return Collections.emptyList()
        }*/
        val listType: Type = object : TypeToken<List<DayOfWeek?>?>() {}.type
        return gson.fromJson(data, listType)
    }


}