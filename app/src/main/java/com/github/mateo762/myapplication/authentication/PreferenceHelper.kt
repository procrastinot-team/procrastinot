package com.github.mateo762.myapplication.authentication

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    private const val LOGIN_PREF = "login_pref"
    private const val IS_LOGGED_IN = "is_logged_in"

    private fun getSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE)
    }

    fun setLoggedIn(context: Context, loggedIn: Boolean) {
        getSharedPreference(context).edit().putBoolean(IS_LOGGED_IN, loggedIn).apply()
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreference(context).getBoolean(IS_LOGGED_IN, false)
    }
}