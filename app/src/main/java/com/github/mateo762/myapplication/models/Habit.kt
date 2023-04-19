package com.github.mateo762.myapplication.models

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@RequiresApi(Build.VERSION_CODES.O)
@Serializable
data class Habit(
    val id: String,
    val name: String,
    val days: List<DayOfWeek>,
    val startTime: String,
    val endTime: String
)
/*
    constructor(name: String,days: ArrayList<DayOfWeek>,startTime: String,endTime: String) :this(){
        this.name = name
        this.days = days
        this.startTime = startTime
        this.endTime = endTime
    }

//    public fun setName(name: String) {
//        this.name = name
//    }
//    public fun setDays(days: ArrayList<DayOfWeek>) {
//        this.days = days
//    }
//    public fun setStartTime(startTime: String) {
//        this.startTime = startTime
//    }
//    public fun setEndTime(endTime: String) {
//        this.endTime = endTime
//    }
//    public fun getName(): String {
//        return this.name
//    }
//    public fun getDays(): ArrayList<DayOfWeek> {
//        return this.days
//    }
//    public fun getStartTime(): String {
//        return this.startTime
//    }
//    public fun getEndTime(): String {
//        return this.endTime
//    }

//    public override fun toString():String{
//        return this.name.plus(this.days).plus(this.startTime).plus(this.endTime)
//    }

}*/
