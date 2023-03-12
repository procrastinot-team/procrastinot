package com.github.mateo762.myapplication.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showToast(@StringRes messageResource: Int) =
    Toast.makeText(this, messageResource, Toast.LENGTH_SHORT).show()