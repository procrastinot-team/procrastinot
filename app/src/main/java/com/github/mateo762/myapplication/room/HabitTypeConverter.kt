package com.github.mateo762.myapplication.room

import androidx.room.TypeConverter
import com.github.mateo762.myapplication.models.HabitEntity
import com.github.mateo762.myapplication.models.HabitImageEntity
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

    @TypeConverter
    fun habitImageEntityListToJson(habitImageEntityList: List<HabitImageEntity>?): String? {
        return gson.toJson(habitImageEntityList)
    }

    @TypeConverter
    fun jsonToHabitImageEntityList(json: String?): List<HabitImageEntity>? {
        if (json == null) return null
        val type = object : TypeToken<List<HabitImageEntity>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun stringListToJson(stringList: List<String>?): String? {
        return gson.toJson(stringList)
    }

    @TypeConverter
    fun jsonToStringList(json: String?): List<String>? {
        if (json == null) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

}