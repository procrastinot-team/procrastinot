package com.github.mateo762.myapplication

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ToastMatcher : TypeSafeMatcher<Root?>() {
    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    public override fun matchesSafely(root: Root?): Boolean {
        val type = root?.windowLayoutParams?.get()?.type
        if (type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY) {
            val windowToken = root.decorView?.windowToken
            val appToken = root.decorView?.applicationWindowToken
            if (windowToken === appToken) {
                return true
            }
        }
        return false
    }
}