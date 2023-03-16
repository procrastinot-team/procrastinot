package com.github.procrastinotteam.procrastinot.settings

import android.os.Bundle
import com.github.procrastinotteam.procrastinot.BaseActivity
import com.github.procrastinotteam.procrastinot.R


class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        super.onCreateDrawer()
    }
}