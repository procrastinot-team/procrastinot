package com.github.mateo762.myapplication.habits.fragments.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import com.alamkanak.weekview.WeekView

class CustomWeekView(context: Context, attrs: AttributeSet) : WeekView(context, attrs) {
    private var startX = 0f

    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f // Set the border width here
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                val distanceX = Math.abs(event.x - startX)
                if (distanceX > 0) {
                    // Ignore horizontal scrolling
                    return false
                }
            }
        }
        return super.onTouchEvent(event)
    }

}