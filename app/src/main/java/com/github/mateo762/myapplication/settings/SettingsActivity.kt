package com.github.mateo762.myapplication.settings

import android.os.Bundle
import com.github.mateo762.myapplication.BaseActivity
import com.github.mateo762.myapplication.R


class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        super.onCreateDrawer()
    }
}