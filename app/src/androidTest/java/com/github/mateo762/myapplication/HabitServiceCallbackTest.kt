package com.github.mateo762.myapplication

import com.github.mateo762.myapplication.habits.HabitServiceCallback
import org.junit.Assert.assertEquals
import org.junit.Test

class HabitServiceCallbackTest {

    private var onSuccessCalled = false
    private var onFailureCalled = false

    @Test
    fun testOnSuccess() {
        // Create an instance of the callback
        val callback = object : HabitServiceCallback {
            override fun onSuccess() {
                onSuccessCalled = true
            }

            override fun onFailure() {
                onFailureCalled = true
            }
        }

        // Invoke the onSuccess method
        callback.onSuccess()

        // Check if onSuccess was called
        assertEquals(true, onSuccessCalled)
        assertEquals(false, onFailureCalled)
    }

    @Test
    fun testOnFailure() {
        // Create an instance of the callback
        val callback = object : HabitServiceCallback {
            override fun onSuccess() {
                onSuccessCalled = true
            }

            override fun onFailure() {
                onFailureCalled = true
            }
        }

        // Invoke the onFailure method
        callback.onFailure()

        // Check if onFailure was called
        assertEquals(false, onSuccessCalled)
        assertEquals(true, onFailureCalled)
    }
}
