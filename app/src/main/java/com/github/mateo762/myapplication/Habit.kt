package com.github.mateo762.myapplication

import java.time.DayOfWeek

data class Habit(
    var name: String,
    var days: ArrayList<DayOfWeek>,
    var startTime: String,
    var endTime: String)
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
